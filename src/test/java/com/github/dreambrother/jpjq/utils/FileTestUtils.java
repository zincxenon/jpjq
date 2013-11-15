package com.github.dreambrother.jpjq.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class FileTestUtils {

    private FileTestUtils() {}

    public static File createTmpDir() {
        try {
            return Files.createTempDirectory("queue-lt").toFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
