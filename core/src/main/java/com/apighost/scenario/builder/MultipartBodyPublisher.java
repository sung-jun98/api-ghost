package com.apighost.scenario.builder;

import com.apighost.parser.template.TemplateConvertor;
import com.apighost.scenario.util.BasePathHolder;
import com.apighost.util.file.FileType;
import com.apighost.util.file.FileUtil;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class MultipartBodyPublisher {

    private static final String LINE_FEED = "\r\n";
    private static final long MAX_FILE_SIZE = 104_857_600; // 100MB

    private final List<Supplier<InputStream>> inputStreamSuppliers = new ArrayList<>();
    private final String boundary = UUID.randomUUID().toString();
    private final Map<String, Object> store;

    public MultipartBodyPublisher(Map<String, Object> store) {
        this.store = store;
    }

    public String getBoundary() {
        return boundary;
    }

    public void addTextPart(String name, String value, String contentType) {
        String convertedValue = TemplateConvertor.convert(value, store);
        System.out.println("=== FormData Debug ===");
        System.out.println("Name: " + name);
        System.out.println("Value: " + convertedValue);
        System.out.println("ContentType: " + contentType);
        System.out.println("=========================");

        StringBuilder header = new StringBuilder();
        header.append("--").append(boundary).append(LINE_FEED)
            .append("Content-Disposition: form-data; name=\"")
            .append(name.replace("\"", "\\\"")).append("\"")
            .append(LINE_FEED)
            .append("Content-Type: ").append(contentType).append(LINE_FEED)
            .append(LINE_FEED);

        byte[] headerBytes = header.toString().getBytes(StandardCharsets.UTF_8);
        byte[] valueBytes = convertedValue.getBytes(StandardCharsets.UTF_8);
        inputStreamSuppliers.add(() -> new SequenceInputStream(
            Collections.enumeration(List.of(
                new ByteArrayInputStream(headerBytes),
                new ByteArrayInputStream(valueBytes),
                new ByteArrayInputStream(LINE_FEED.getBytes(StandardCharsets.UTF_8))
            ))));
    }

    public void addFilePart(String name, String fileName, String contentType) {
        System.out.println("=== FormData Debug ===");
        System.out.println("Name: " + name);
        System.out.println("FileName: " + fileName);
        System.out.println("ContentType: " + contentType);
        System.out.println("=========================");
        System.out.println("Processing file: " + fileName);

        StringBuilder header = new StringBuilder();
        header.append("--").append(boundary).append(LINE_FEED)
            .append("Content-Disposition: form-data; name=\"")
            .append(name.replace("\"", "\\\"")).append("\"; filename=\"")
            .append(fileName.replace("\"", "\\\"")).append("\"")
            .append(LINE_FEED)
            .append("Content-Type: ").append(contentType).append(LINE_FEED)
            .append(LINE_FEED);

        byte[] headerBytes = header.toString().getBytes(StandardCharsets.UTF_8);
        Path baseDir = FileUtil.findDirectory(FileType.RESOURCES, BasePathHolder.getInstance()
            .getBasePath());
        Path filePath = baseDir.resolve(fileName);
        System.out.println("Attempting to load file: " + filePath);

        try {
            if (!Files.exists(filePath)) {
                System.out.println("Warning: File not found: " + filePath + ". Adding empty content.");
                inputStreamSuppliers.add(() -> new SequenceInputStream(
                    Collections.enumeration(List.of(
                        new ByteArrayInputStream(headerBytes),
                        new ByteArrayInputStream(new byte[0]),
                        new ByteArrayInputStream(LINE_FEED.getBytes(StandardCharsets.UTF_8))
                    ))));
            } else {
                long fileSize = Files.size(filePath);
                boolean isLargeFile = fileSize > MAX_FILE_SIZE;
                System.out.println("File size: " + fileSize + " bytes, isLargeFile: " + isLargeFile);
                inputStreamSuppliers.add(() -> {
                    try {
                        InputStream fileInputStream = Files.newInputStream(filePath);
                        if (!isLargeFile) {
                            byte[] fileContentBytes = fileInputStream.readAllBytes();
                            fileInputStream.close();
                            return new SequenceInputStream(
                                Collections.enumeration(List.of(
                                    new ByteArrayInputStream(headerBytes),
                                    new ByteArrayInputStream(fileContentBytes),
                                    new ByteArrayInputStream(LINE_FEED.getBytes(StandardCharsets.UTF_8))
                                )));
                        } else {
                            return new SequenceInputStream(
                                Collections.enumeration(List.of(
                                    new ByteArrayInputStream(headerBytes),
                                    fileInputStream,
                                    new ByteArrayInputStream(LINE_FEED.getBytes(StandardCharsets.UTF_8))
                                )));
                        }
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to process file: " + filePath, e);
                    }
                });
            }
        } catch (IOException e) {
            System.out.println("Warning: Failed to process file: " + fileName);
            inputStreamSuppliers.add(() -> new SequenceInputStream(
                Collections.enumeration(List.of(
                    new ByteArrayInputStream(headerBytes),
                    new ByteArrayInputStream(new byte[0]),
                    new ByteArrayInputStream(LINE_FEED.getBytes(StandardCharsets.UTF_8))
                ))));
        }
    }

    public HttpRequest.BodyPublisher build() {
        byte[] closingBoundary = ("--" + boundary + "--" + LINE_FEED).getBytes(StandardCharsets.UTF_8);
        inputStreamSuppliers.add(() -> new ByteArrayInputStream(closingBoundary));
        return HttpRequest.BodyPublishers.ofInputStream(() -> new SequenceInputStream(
            Collections.enumeration(inputStreamSuppliers.stream()
                .map(Supplier::get)
                .collect(Collectors.toList()))));
    }
}