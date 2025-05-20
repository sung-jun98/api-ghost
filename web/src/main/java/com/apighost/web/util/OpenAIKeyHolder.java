package com.apighost.web.util;

import com.apighost.model.config.ToolConfig;
import com.apighost.util.file.FileType;
import com.apighost.util.file.FileUtil;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * A class that imports private Openaikey from an individual computer
 *
 * @author sung-jun98
 * @version BETA-0.0.1
 */
public class OpenAIKeyHolder {

    /**
     * 1. Read the files in the user's user/.apigost/api-gost.yaml 'path.
     * 2. parsing the imported YAML file
     * 3. Import Openaikey in the YAML file.
     *
     * @return openAiKey from `user/.apighost/api-gost.yaml
     * @throws IOException
     */
    public String getOpenAIKey() throws IOException {

        Path configDirectory = FileUtil.findDirectory(FileType.CONFIG, BasePathHolder.getInstance()
            .getBasePath());
        Path yamlConfigFilePath = configDirectory.resolve("api-ghost.yaml");
        if (!Files.exists(yamlConfigFilePath)) {
            yamlConfigFilePath = configDirectory.resolve("api-ghost.yml");
            if (!Files.exists(yamlConfigFilePath)) {
                throw new IOException("No api-ghost config file");
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
            || toolConfig.getApighost().getTool().getOpenAiKey() == null
            || toolConfig.getApighost().getTool().getOpenAiKey().isEmpty()) {

            throw new IOException("No OpenAI Key");
        }

        String openAiKey = toolConfig.getApighost().getTool().getOpenAiKey();

        return openAiKey;
    }
}
