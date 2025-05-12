package com.apighost.web.collector;

import com.apighost.model.collector.Endpoint;
import com.apighost.model.config.ToolConfig;
import com.apighost.util.file.FileType;
import com.apighost.util.file.FileUtil;
import com.apighost.web.util.BasePathHolder;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Collector class that scans the configured API Ghost configuration file and fetches endpoint
 * metadata from external service URLs.
 *
 * <p>It supports YAML configuration files named <code>api-ghost.yaml</code> or
 * <code>api-ghost.yml</code>, which define the list of service base URLs.</p>
 *
 * @author haazz
 * @version BETA-0.0.1
 */
public class ApiCollector {

    /**
     * Retrieves a list of {@link Endpoint} objects by reading the local configuration file and
     * sending HTTP requests to configured library endpoints.
     *
     * <p>This method performs the following steps:
     * <ul>
     *   <li>Locates the API Ghost YAML configuration file.</li>
     *   <li>Parses the file into a {@link ToolConfig} object.</li>
     *   <li>Sends a GET request to each library's <code>/apighost/endpoint-json</code> endpoint.</li>
     *   <li>Parses the response as a list of endpoints and aggregates them.</li>
     * </ul>
     * </p>
     *
     * @return a combined list of {@link Endpoint} objects from all configured libraries
     * @throws IOException if reading the configuration file fails
     */
    public List<Endpoint> getEndPointList() throws IOException {
        List<Endpoint> endpointList = new ArrayList<>();
        Path configDirectory = FileUtil.findDirectory(FileType.CONFIG, BasePathHolder.getInstance()
            .getBasePath());
        Path yamlConfigFilePath = configDirectory.resolve("api-ghost.yaml");
        if (!Files.exists(yamlConfigFilePath)) {
            yamlConfigFilePath = configDirectory.resolve("api-ghost.yml");
            if (!Files.exists(yamlConfigFilePath)) {
                return endpointList;
            }
        }

        ObjectMapper objectMapper = YAMLMapper.builder()
            .disable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY)
            .disable(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS)
            .build();
        ToolConfig toolConfig = objectMapper.readValue(Files.newBufferedReader(yamlConfigFilePath),
            ToolConfig.class);
        if (toolConfig == null || toolConfig.getApighost() == null
            || toolConfig.getApighost().getTool() == null
            || toolConfig.getApighost().getTool().getLibs() == null
            || toolConfig.getApighost().getTool().getLibs().isEmpty()) {
            return endpointList;
        }

        HttpClient httpClient = HttpClient.newHttpClient();
        objectMapper = new ObjectMapper();

        for (String url : toolConfig.getApighost().getTool().getLibs()) {
            try {
                HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url + "/apighost/endpoint-json"))
                    .GET()
                    .build();

                HttpResponse<String> response = httpClient.send(request,
                    HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 200) {
                    List<Endpoint> endpoint = objectMapper.readValue(response.body(), List.class);
                    endpointList.addAll(endpoint);
                }
            } catch (Exception e) {
                continue;
            }
        }
        return endpointList;
    }
}
