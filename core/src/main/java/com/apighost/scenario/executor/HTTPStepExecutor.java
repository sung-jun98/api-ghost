package com.apighost.scenario.executor;

import com.apighost.model.scenario.request.FormData;
import com.apighost.model.scenario.step.HTTPMethod;
import com.apighost.parser.flattener.Flattener;
import com.apighost.parser.flattener.JsonFlattener;
import com.apighost.parser.template.TemplateConvertor;
import com.apighost.model.scenario.result.ResultStep;
import com.apighost.model.scenario.step.Route;
import com.apighost.model.scenario.step.Step;
import com.apighost.model.scenario.request.Request;
import com.apighost.model.scenario.step.Then;
import com.apighost.scenario.builder.MultipartBodyPublisher;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Executor implementation for handling HTTP-based steps in a scenario.
 * <p>
 * This class builds and sends HTTP requests based on the {@link Request} configuration, applies
 * template substitution from the shared store, flattens the response for validation, and determines
 * the next step based on the matched route.
 * </p>
 * <p>
 * Supported features include:
 * <ul>
 *   <li>GET, POST, PUT, DELETE, PATCH, HEAD, OPTIONS, TRACE, CONNECT methods</li>
 *   <li>Header and JSON body templating</li>
 *   <li>Flattener for response body validation</li>
 *   <li>Dynamic routing with conditional {@code Then} clauses</li>
 * </ul>
 *
 * @author haazz
 * @version BETA-0.0.1
 */
public class HTTPStepExecutor implements StepExecutor {

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private Flattener flattener;
    private MultipartBodyPublisher publisher;

    /**
     * Executes a given HTTP step and evaluates its result based on response status and expected
     * values.
     *
     * @param stepKey   the unique identifier of the step in the scenario
     * @param step      the {@link Step} object defining the execution logic
     * @param store     a shared key-value store for data persistence across steps (e.g., extracted
     *                  variables)
     * @param timeoutMs maximum allowed execution time in milliseconds for this step
     * @return {@link ResultStep} containing the outcome of the step execution
     * @throws IOException          if an I/O error occurs during step execution
     * @throws InterruptedException if the execution is interrupted (e.g., due to timeout or thread
     *                              interruption)
     */
    @Override
    public ResultStep execute(String stepKey, Step step, Map<String, Object> store, long timeoutMs)
        throws IOException, InterruptedException {
        Request request = step.getRequest();
        HttpRequest.Builder builder = HttpRequest.newBuilder()
            .uri(URI.create(request.getUrl()))
            .timeout(java.time.Duration.ofMillis(timeoutMs));

        String contentType =
            request.getHeader() != null ? request.getHeader().get("Content-Type") : null;
        Map<String, String> headers =
            request.getHeader() != null ? new HashMap<>(request.getHeader()) : new HashMap<>();

        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.noBody();
        if (request.getBody() != null) {
            if (request.getBody().getJson() != null) {
                body = HttpRequest.BodyPublishers.ofString(
                    TemplateConvertor.convert(request.getBody().getJson(), store));
                if (contentType == null) {
                    headers.put("Content-Type", "application/json");
                }
            } else if (request.getBody().getFormdata() != null) {
                FormData formData = request.getBody().getFormdata();
                if (contentType != null && contentType.startsWith("multipart/form-data")) {
                    publisher = new MultipartBodyPublisher(store);
                    if (formData.getText() != null) {
                        for (Map.Entry<String, String> textEntry : formData.getText().entrySet()) {
                            String name = textEntry.getKey();
                            String value = TemplateConvertor.convert(textEntry.getValue(), store);
                            publisher.addTextPart(name, value, "text/plain");
                        }
                    }
                    if (formData.getFile() != null) {
                        for (Map.Entry<String, String> fileEntry : formData.getFile().entrySet()) {
                            String name = fileEntry.getKey();
                            String fileName = fileEntry.getValue();
                            publisher.addFilePart(name, fileName, "application/octet-stream");
                        }
                    }
                    body = publisher.build();
                    headers.put("Content-Type",
                        "multipart/form-data; boundary=" + publisher.getBoundary());
                } else if (contentType != null && contentType.equals(
                    "application/x-www-form-urlencoded")) {
                    body = buildUrlEncodedFormData(formData.getText(), store);
                    headers.put("Content-Type", contentType);
                } else if (contentType != null && contentType.equals("text/plain")) {
                    body = buildTextPlainFormData(formData.getText(), store);
                    headers.put("Content-Type", contentType);
                } else {
                    throw new IllegalArgumentException("Unsupported Content-Type: " + contentType);
                }
            }
        } else if (request.getMethod() == HTTPMethod.GET) {
            headers.remove("Content-Type");
            headers.remove("Content-Length");
            store.remove("contentType");
            store.remove("Content-Type");
        }

        convertMapStringTemplate(headers, store);
        headers.forEach(builder::header);

        HttpRequest httpRequest;
        switch (request.getMethod()) {
            case GET -> httpRequest = builder.GET().build();
            case POST -> httpRequest = builder.POST(body).build();
            case PUT -> httpRequest = builder.PUT(body).build();
            case DELETE, PATCH, HEAD, OPTIONS, TRACE, CONNECT ->
                httpRequest = builder.method(request.getMethod().name(), body).build();
            default ->
                throw new UnsupportedOperationException("Unknown method: " + request.getMethod());
        }

        System.out.println("=== HTTP Request Debug ===");
        System.out.println("Method: " + httpRequest.method());
        System.out.println("URI: " + httpRequest.uri());
        System.out.println("Headers: " + httpRequest.headers().map());
        System.out.println(
            "Content-Type: " + httpRequest.headers().firstValue("Content-Type").orElse("N/A"));
        System.out.println("=========================");

        long start = System.currentTimeMillis();
        HttpResponse<String> httpResponse = httpClient.send(httpRequest,
            HttpResponse.BodyHandlers.ofString());
        long end = System.currentTimeMillis();

        Map<String, String> flatResponseHeader = responseHeaderParser(httpResponse.headers().map());
        Map<String, Object> flatResponseBody = null;
        if ("application/json".equals(flatResponseHeader.get("Content-Type"))) {
            flattener = new JsonFlattener(new ObjectMapper());
            flatResponseBody = flattener.flatten(httpResponse.body());
        }

        Then matchedThen = matchExpected(httpResponse, flatResponseBody, step.getRoute());
        String nextStep = null;
        boolean isRequestSuccess = false;
        if (matchedThen != null) {
            nextStep = executeThen(matchedThen, flatResponseBody, store);
            isRequestSuccess = true;
        }

        return new ResultStep.Builder()
            .stepName(stepKey)
            .type(step.getType())
            .method(request.getMethod())
            .url(request.getUrl())
            .requestHeader(request.getHeader())
            .requestBody(request.getBody())
            .responseHeaders(flatResponseHeader)
            .responseBody(httpResponse.body())
            .status(httpResponse.statusCode())
            .startTime(Instant.ofEpochMilli(start).toString())
            .endTime(Instant.ofEpochMilli(end).toString())
            .durationMs(end - start)
            .isRequestSuccess(isRequestSuccess)
            .nextStep(nextStep)
            .route(step.getRoute())
            .build();
    }

    private Map<String, String> responseHeaderParser(Map<String, List<String>> httpResponseHeader) {
        return httpResponseHeader.entrySet().stream()
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                entry -> String.join(", ", entry.getValue())
            ));
    }

    private Then matchExpected(HttpResponse<String> httpResponse,
        Map<String, Object> flatResponseBody, List<Route> routeList) {
        for (Route route : routeList) {

            if (route.getExpected() == null || route.getExpected().getValue() == null) {
                return route.getThen();
            }

            if (isMatchExpectedStatus(httpResponse.statusCode(), route.getExpected().getStatus())
                && isMatchExpectedValue(flatResponseBody, route.getExpected().getValue())) {
                return route.getThen();
            }
        }
        return null;
    }

    private boolean isMatchExpectedStatus(int statusCode, String expectedStatus) {
        if (expectedStatus == null || expectedStatus.isEmpty()) {
            return true;
        }

        String[] statusRange = expectedStatus.split("-");

        try {
            if (statusRange.length == 2) {
                if (Integer.parseInt(statusRange[0]) <= statusCode
                    && Integer.parseInt(statusRange[1]) >= statusCode) {
                    return true;
                }
            } else if (statusRange.length == 1) {
                if (Integer.parseInt(statusRange[0]) == statusCode) {
                    return true;
                }
            } else {
                throw new IllegalArgumentException(
                    "Invalid HTTP status pattern: " + expectedStatus);
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid HTTP status pattern: " + expectedStatus, e);
        }

        return false;
    }

    private boolean isMatchExpectedValue(Map<String, Object> flatResponseBody,
        Map<String, Object> expectedValue) {
        if (flatResponseBody == null || expectedValue == null || expectedValue.isEmpty()) {
            return true;
        }
        for (Map.Entry<String, Object> entry : expectedValue.entrySet()) {
            if (!flatResponseBody.containsKey(entry.getKey())) {
                return false;
            }
            Object value = flatResponseBody.get(entry.getKey());
            if (!entry.getValue().equals(value)) {
                return false;
            }
        }
        return true;
    }

    private String executeThen(Then then, Map<String, Object> flatResponseBody,
        Map<String, Object> store) {
        if (then == null || then.getStep() == null) {
            return null;
        }
        if (then.getStore() != null) {
            Map<String, Object> newStore = then.getStore();
            convertMapObjectTemplate(newStore, flatResponseBody);
            convertMapObjectTemplate(newStore, store);
            store.putAll(newStore);
        }

        return then.getStep();
    }

    private void convertMapStringTemplate(Map<String, String> map, Map<String, Object> store) {
        if (map == null || map.isEmpty()) {
            return;
        }

        for (Map.Entry<String, String> entry : map.entrySet()) {
            String originalValue = entry.getValue();
            String convertedValue = TemplateConvertor.convert(originalValue, store);
            entry.setValue(convertedValue);
        }
    }

    private void convertMapObjectTemplate(Map<String, Object> map, Map<String, Object> store) {
        if (map == null || map.isEmpty() || store == null || store.isEmpty()) {
            return;
        }

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (!(entry.getValue() instanceof String)) {
                continue;
            }
            String originalValue = (String) entry.getValue();
            String convertedValue = TemplateConvertor.convert(originalValue, store);
            entry.setValue(convertedValue);
        }
    }

    private HttpRequest.BodyPublisher buildUrlEncodedFormData(Map<String, String> text,
        Map<String, Object> store) {
        if (text == null || text.isEmpty()) {
            return HttpRequest.BodyPublishers.ofString("");
        }
        String formDataString = text.entrySet().stream()
            .map(entry -> {
                String name = URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8);
                String value = URLEncoder.encode(TemplateConvertor.convert(entry.getValue(), store),
                    StandardCharsets.UTF_8);
                return name + "=" + value;
            })
            .collect(Collectors.joining("&"));
        return HttpRequest.BodyPublishers.ofString(formDataString);
    }

    private HttpRequest.BodyPublisher buildTextPlainFormData(Map<String, String> text,
        Map<String, Object> store) {
        if (text == null || text.isEmpty()) {
            return HttpRequest.BodyPublishers.ofString("");
        }
        String formDataString = text.entrySet().stream()
            .map(entry -> {
                String value = TemplateConvertor.convert(entry.getValue(), store);
                return entry.getKey() + "=" + value.replace(" ", "+");
            })
            .collect(Collectors.joining("\n"));
        return HttpRequest.BodyPublishers.ofString(formDataString);
    }

}
