package com.booking.utils;

import com.booking.data.Constants;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class MockHtmlReader {

    public static String load(String fileName) {
        try {
            Path filePath = Path.of("src/main/resources/html/", fileName);
            return Files.readString(filePath, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(Constants.FAILED_TO_LOAD_MOCK_HTML + fileName, e);
        }
    }
}

