package com.apighost.parser.loadtest.writer;

import java.io.IOException;

/**
 * interface for LoadTestParmeter file writer (DTO -> YAML)
 *
 * @author sung-jun98
 * @version BETA-0.0.1
 */
public interface LoadTestParameterWriter {

    public void writeLoadTestParameter(LoadTestParameter loadTestParameter, String filePath)
        throws IOException;
}
