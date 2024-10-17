package org.acme.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.acme.dto.GetUserCountCreationPerCompanyPerYear;
import org.acme.entity.User;
import org.acme.repository.UserRepository;
import org.jboss.logging.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@ApplicationScoped
public class UserService {
    private static final Logger LOG = Logger.getLogger(UserService.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Inject
    private UserRepository userRepository;

    @Inject
    private FileService fileService;


    @Transactional
    public User findOrCreateUser(String email) {
        LOG.info("Executing findOrCreateUser on thread: " + Thread.currentThread().getName());

        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        var startTime = System.currentTimeMillis();
        User existingUser = userRepository.findByEmail(email);
        var took = System.currentTimeMillis() - startTime;
        LOG.info("Completed random_sleep stored procedure in blocking. took = {} " + took + " ms");

        if (existingUser != null) {
            LOG.info("User email is : " + existingUser.getEmail());
            return existingUser;
        }

        // If user is not found, create a new one
        User newUser = new User(email);
        userRepository.persist(newUser);
        LOG.info("User email is : " + newUser.getEmail());
        return newUser;
    }

    public void invokeRandomSleepProcedure() {
        // Calling the stored procedure using native query
        var startTime = System.currentTimeMillis();
        LOG.info("Invoking random_sleep stored procedure on thread: " + Thread.currentThread().getName());
        entityManager.createStoredProcedureQuery("random_sleep").execute();
        var took = System.currentTimeMillis() - startTime;
        LOG.info("Completed random_sleep stored procedure in blocking. took = {} " + took + " ms");
    }

    //
//    public void invokeRandomSleepProcedure() {
//        var startTime = System.currentTimeMillis();
//        // Using createStoredProcedureQuery to call the stored procedure
//        var storedProcedure = entityManager.createStoredProcedureQuery("random_sleep");
//        // Execute and get the OUT parameter
//        storedProcedure.execute();
////        int a = (Integer) storedProcedure.getOutputParameterValue(1);
////        LOG.info("break is of " + a + " ms");
//        var took = System.currentTimeMillis() - startTime;
//        LOG.info("Completed random_sleep stored procedure. took = {} " + took + " ms");
//
//    }


    public String getFileContext(final String path) {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("files/input.txt");
        if (inputStream == null) {
            System.out.println("input stream is null");
            return "";
        }

        // Read the file content
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            return reader.lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public List<GetUserCountCreationPerCompanyPerYear> getUserCountPerCompany() throws IOException {
        final String input = getFileContext("files/input.txt");

        final List<Integer> inputList = Arrays.stream(input.split(",")).map(Integer::parseInt).toList();

        System.out.println(inputList);

        final List<GetUserCountCreationPerCompanyPerYear> list = userRepository.getCompanyByUserCount(inputList);

        return list;
    }

}