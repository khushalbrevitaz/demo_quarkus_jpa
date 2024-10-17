package org.acme.service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@ApplicationScoped
public class FileService {
    private BufferedReader bufferedReader;

    // Initialize the BufferedReader once when the application starts
    @PostConstruct
    public void init() throws IOException {
        Path path = Paths.get("/app/resources/files/input.txt");
        bufferedReader = Files.newBufferedReader(path);
    }

    // Method to read the first line using the single BufferedReader instance
    public String readFirstLine() throws IOException {
        return bufferedReader.readLine();
    }

    // Clean up the BufferedReader when the application is stopped
    @PreDestroy
    public void cleanup() throws IOException {
        if (bufferedReader != null) {
            bufferedReader.close();
        }
    }
}
