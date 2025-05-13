package com.apighost.parser.loadtest.writer;

import com.apighost.model.loadtest.parameter.LoadTestParameter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.io.File;
import java.io.IOException;

/**
 * Java DTO -> YAML parser
 *
 * @author sung-jun98
 * @version BETA-0.0.1
 */
public class YamlLoadTestParameterWriter implements LoadTestParameterWriter {

    private final ObjectMapper objectMapper;

    public YamlLoadTestParameterWriter() {
        this.objectMapper = new ObjectMapper(new YAMLFactory());
        /** Format the output in a human-readable (pretty-printed) way. */
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    /**
     * @param loadTestParameter
     * @param filePath
     * @throws IOException
     */
    @Override
    public void writeLoadTestParameter(LoadTestParameter loadTestParameter, String filePath)
        throws IOException {

        File yamlFile = new File(filePath);

        try {
            objectMapper.writeValue(yamlFile, loadTestParameter);
        } catch (IOException e) {
            throw new IOException("Error occurs while writing YAML files", e);
        }

    }
}
