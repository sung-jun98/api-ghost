package com.apighost.parser.loadtest.writer;

import com.apighost.model.loadtest.parameter.LoadTestParameter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.io.File;
import java.io.IOException;

/**
 * Create a loadtest setting file on the path you received as a parameter.
 *
 * @author sung-jun98
 * @version BETA-0.0.1
 */
public class YamlLoadTestParameterWriter implements LoadTestParameterWriter {

    private final ObjectMapper objectMapper;

    /**
     * Constructs a YamlLoadTestParameterWriter instance with a configured ObjectMapper
     * for processing YAML files.
     */
    public YamlLoadTestParameterWriter() {
        this.objectMapper = new ObjectMapper(new YAMLFactory());
        /** Format the output in a human-readable (pretty-printed) way. */
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    /**
     * Create a loadtest setting file on the path you received as a parameter.
     *
     * @param loadTestParameter
     * @param filePath
     * @throws IOException
     */
    @Override
    public void writeLoadParam(LoadTestParameter loadTestParameter, String filePath)
        throws IOException {

        File yamlFile = new File(filePath);

        try {
            objectMapper.writeValue(yamlFile, loadTestParameter);
        } catch (IOException e) {
            throw new IOException("Error occurs while writing YAML files", e);
        }

    }
}
