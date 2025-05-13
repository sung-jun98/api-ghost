package com.apighost.parser.loadtest.reader;

import com.apighost.model.loadtest.parameter.LoadTestParameter;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import java.io.File;
import java.io.IOException;

/**
 * yaml -> DTO reader
 *
 * @author sung-jun98
 * @version BETA-0.0.1
 */
public class YamlLoadTestParameterReader implements LoadTestParameterReader {
    ObjectMapper objectMapper;

    /**
     * Initialization of YAML Objectmapper.
     */
    public YamlLoadTestParameterReader() {

        objectMapper = YAMLMapper.builder()
                           .disable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY)
                           .disable(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS)
                           .build();
    }

    /**
     * YAML reader. it converts YAML files to DTO
     *
     * @param filePath
     * @return LoadTestParameter
     * @throws IOException
     */
    @Override
    public LoadTestParameter readLoadParam (String filePath) throws IOException {
        File inputFile = new File(filePath);
        if (!inputFile.isFile()) {
            throw new IOException("Invalid file path: " + filePath);
        }
        return objectMapper.readValue(inputFile, LoadTestParameter.class);
    }
}
