package com.apighost.parser.loadtest.writer;

import com.apighost.model.loadtest.parameter.LoadTestParameter;
import java.io.IOException;

/**
 * JavaDto -> YAML convert interface.
 * this interface makes LoadTestParameter files.
 *
 * @author sung-jun98
 * @version BETA-0.0.1
 */
public interface LoadTestParameterWriter {

    public void writeLoadParam(LoadTestParameter loadTestParameter, String filePath)
        throws IOException;
}
