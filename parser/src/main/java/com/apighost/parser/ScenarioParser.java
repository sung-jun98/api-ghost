package com.apighost.parser;
import java.io.*;
import java.nio.file.Path;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.apighost.model.scenario.Scenario;

public class ScenarioParser {
    private final ObjectMapper objectMapper;

    public ScenarioParser() {
        // YAML 파싱을 위한 ObjectMapper 초기화
        this.objectMapper = new ObjectMapper(new YAMLFactory());
    }

    /**
     * 파일 경로로부터 시나리오 YAML을 파싱
     *
     * @param filePath YAML 파일 경로
     * @return 파싱된 ScenarioDTO 객체
     * @throws IOException 파일 읽기 또는 파싱 실패 시
     */
    public Scenario parseFromFile(String filePath) throws IOException {
        File file = new File(filePath);
        return objectMapper.readValue(file, Scenario.class);
    }

    /**
     * Path 객체로부터 시나리오 YAML을 파싱
     *
     * @param path YAML 파일 Path
     * @return 파싱된 ScenarioDTO 객체
     * @throws IOException 파일 읽기 또는 파싱 실패 시
     */
    public Scenario parseFromPath(Path path) throws IOException {
        return objectMapper.readValue(path.toFile(), Scenario.class);
    }

    /**
     * 문자열로부터 시나리오 YAML을 파싱
     *
     * @param yamlContent YAML 내용 문자열
     * @return 파싱된 ScenarioDTO 객체
     * @throws IOException 파싱 실패 시
     */
    public Scenario parseFromString(String yamlContent) throws IOException {
        return objectMapper.readValue(yamlContent, Scenario.class);
    }

    /**
     * InputStream으로부터 시나리오 YAML을 파싱
     *
     * @param inputStream YAML 내용을 제공하는 InputStream
     * @return 파싱된 ScenarioDTO 객체
     * @throws IOException 파싱 실패 시
     */
    public Scenario parseFromInputStream(InputStream inputStream) throws IOException {
        return objectMapper.readValue(inputStream, Scenario.class);
    }

    /**
     * 파싱된 ScenarioDTO를 YAML 문자열로 변환
     *
     * @param scenarioDTO 변환할 ScenarioDTO 객체
     * @return YAML 형식의 문자열
     * @throws IOException 변환 실패 시
     */
    public String convertToYaml(Scenario scenarioDTO) throws IOException {
        return objectMapper.writeValueAsString(scenarioDTO);
    }

    /**
     * 파싱된 ScenarioDTO를 YAML 파일로 저장
     *
     * @param scenarioDTO 저장할 ScenarioDTO 객체
     * @param filePath 저장할 파일 경로
     * @throws IOException 파일 쓰기 실패 시
     */
    public void saveToYamlFile(Scenario scenarioDTO, String filePath) throws IOException {
        objectMapper.writeValue(new File(filePath), scenarioDTO);
    }

    /**
     * 사용 예시를 보여주는 메인 메소드
     */
    public static void main(String[] args) {
        try {
            // 파서 인스턴스 생성
            ScenarioParser parser = new ScenarioParser();

            // 파일에서 파싱 예시
            String filePath = "path/to/scenario.yaml";
            Scenario scenario = parser.parseFromFile(filePath);

            // 파싱 결과 출력
            System.out.println("Parsed Scenario: " + scenario);

            // 각 스텝 정보 출력
            System.out.println("\nSteps information:");
            for (int i = 0; i < scenario.getSteps().size(); i++) {
                Scenario.Step step = scenario.getSteps().get(i);
                System.out.println("Step " + (i + 1) + ": " + step.getName());
                System.out.println("  Method: " + step.getRequest().getMethod());
                System.out.println("  URL: " + step.getRequest().getUrl());

                // 분기 스텝인지 확인
                if (step.isBranch()) {
                    System.out.println("  This is a branch step with condition: " + step.getWhen());
                }

                // 추출 정보가 있는지 확인
                if (step.getExtract() != null && !step.getExtract().isEmpty()) {
                    System.out.println("  Extractions: " + step.getExtract());
                }

                System.out.println();
            }

        } catch (IOException e) {
            System.err.println("Error parsing scenario: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
