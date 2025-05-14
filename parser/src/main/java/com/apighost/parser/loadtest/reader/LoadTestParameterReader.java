package com.apighost.parser.loadtest.reader;
import com.apighost.model.loadtest.parameter.LoadTestParameter;
import java.io.IOException;

/**
 * an interface for yaml -> DTO reader
 *
 * @author sung-jun98
 * @version BETA-0.0.1
 */
public interface LoadTestParameterReader {
    /**
     * Yaml -> Java Dto
     *
     * @param filePath
     * @return
     * @throws IOException
     */
    public LoadTestParameter readLoadParam(String filePath) throws IOException;
}
