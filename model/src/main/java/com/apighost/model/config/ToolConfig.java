package com.apighost.model.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Represents the root configuration for the API Ghost tool. This class is designed to map YAML or
 * JSON configuration files into a strongly typed Java object structure using Jackson.
 *
 * <p>
 * Example YAML structure:
 * <pre>
 * apighost:
 *   tool:
 *     openAiKey: openai-api-key
 *     libs:
 *       - http://localhost:8080
 *       - http://localhost:8000
 * </pre>
 * </p>
 *
 * @author haazz
 * @version BETA-0.0.1
 */
public class ToolConfig {

    private Apighost apighost;

    public Apighost getApighost() {
        return apighost;
    }

    public void setApighost(Apighost apighost) {
        this.apighost = apighost;
    }

    /**
     * Represents the "apighost" node in the configuration file.
     */
    public static class Apighost {

        private Tool tool;

        public Tool getTool() {
            return tool;
        }

        public void setTool(Tool tool) {
            this.tool = tool;
        }
    }

    /**
     * Represents the "tool" node under "apighost". Contains runtime settings such as API keys and
     * library URLs.
     */
    public static class Tool {

        private String openAiKey;
        private List<String> libs;

        @JsonProperty("openAiKey")
        public String getOpenAiKey() {
            return openAiKey;
        }

        public void setOpenAiKey(String openAiKey) {
            this.openAiKey = openAiKey;
        }

        @JsonProperty("libs")
        public List<String> getLibs() {
            return libs;
        }

        public void setLibs(List<String> libs) {
            this.libs = libs;
        }
    }
}
