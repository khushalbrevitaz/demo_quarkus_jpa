package org.acme.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import org.acme.entity.User;
import org.acme.rest.client.LambdaFunctionClient;
import org.acme.service.UserService;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

@Path("/users")
public class UserResource {

    private static final Logger log = LoggerFactory.getLogger(UserResource.class);

    @Inject
    UserService userService;

    @Inject
    @RestClient
    LambdaFunctionClient lambdaFunctionClient;

    @GET
    @Path("/simulate")
    @Produces(MediaType.TEXT_HTML)
    public String simulate() throws Exception {
        log.info("simulation request time = {}", System.currentTimeMillis());
        final Random random = new Random();
        Thread.sleep(random.nextInt(101) + 100);
        return "hello";
    }

    @GET
    @Path("/simulate2")
    @Produces(MediaType.TEXT_HTML)
    public String simulate2() {
        log.info("simulation request @ = {}", System.currentTimeMillis());
        return "hello";
    }

    @GET
    @Path("/simulate3")
    @Produces(MediaType.TEXT_HTML)
    @Retry(maxRetries = 3)
    public String simulate3() {
        log.info("blocking: calling AWS lambda for simulation");
        return lambdaFunctionClient.simulate();
    }


    @GET
    @Path("/findOrCreate")
    @Produces(MediaType.APPLICATION_JSON)
    public User findOrCreateUser(@QueryParam("email") String email) {
        return userService.findOrCreateUser(email);
    }

    @GET
    @Path("/simulate4")
    @Produces(MediaType.APPLICATION_JSON)
    public void simulateIODelay() {
        userService.callStoredProcedureWithFakeDelay();
    }

    private final String SAMPLE_TXT_FILE_PATH = "sample.txt";

    public String countWords() throws IOException {
        //URL fileUrl = getClass().getClassLoader().getResource(SAMPLE_TXT_FILE_PATH);
        // Convert URL to file path (optional)
        //java.nio.file.Path path = Paths.get(fileUrl.getPath());
        java.nio.file.Path path = new File("/deployments/sample.txt").toPath();
        Stream<String> lines = Files.lines(path);
        long wordCount = lines
                .flatMap(line -> Stream.of(line.split("\\s+"))) // Split each line into words
                .filter(word -> !word.isEmpty()) // Filter out empty words (from multiple spaces)
                .count();
        return String.valueOf(wordCount);
    }


    @GET
    @Path("/simulate5")
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> simulate4() throws Exception {
        log.info("simulate4-blocking: random delay + word count + stored procedure call");

        String wordCount = countWords();

        final Random random = new Random();
        Thread.sleep(random.nextInt(101) + 50);

        userService.callStoredProcedureWithFakeDelay();

        return List.of("hello", wordCount);
    }
}
