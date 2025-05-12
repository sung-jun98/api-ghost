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

public class ApiCollector {

    public List<Endpoint> getEndPointList() throws IOException {
        List<Endpoint> endpointList = new ArrayList<>();
        Path configDirectory = FileUtil.findDirectory(FileType.CONFIG, BasePathHolder.getInstance()
            .getBasePath());
        Path yamlConfigFilePath = configDirectory.resolve("api-ghost.yaml");
        System.out.printf("Loading endpoint list from %s\n", yamlConfigFilePath);
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
