package com.apighost.parser.scenario.reader;

import com.apighost.model.scenario.Scenario;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import java.io.File;
import java.io.IOException;

/**
 * JSON implementation of {@link YamlScenarioReader}.
 *
 * <p>Example usage:</p>
 * <pre>
 *     new YamlScenarioReader().readScenario("filPath");
 * </pre>
 *
 * @author sun-jun98
 * @version BETA-0.0.1
 */
public class YamlScenarioReader implements ScenarioReader {

    ObjectMapper objectMapper;

    public YamlScenarioReader() {
//        objectMapper = new ObjectMapper(new YAMLFactory());
//        this.objectMapper.disable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY);
//        this.objectMapper.disable(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS);

        objectMapper = YAMLMapper.builder()
                           .disable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY)
                           .disable(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS)
                           .build();
    }

    /**
     * Convert the YAML scenario information stored in the local directory to DTO
     *
     * @param filePath The path of the YAML file to be read
     * @return {@link Scenario} Scenario information converted to DTO
     * @throws IOException if the file is not found, invalid, or cannot be parsed
     */
    @Override
    public Scenario readScenario(String filePath) throws IOException {
        File inputFile = new File(filePath);
        if (!inputFile.isFile()) {
            throw new IOException("Invalid file path: " + filePath);
        }
        return objectMapper.readValue(inputFile, Scenario.class);
    }
}
