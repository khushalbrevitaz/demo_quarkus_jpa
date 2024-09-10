package org.acme.service;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;

import jakarta.inject.Inject;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import org.acme.entity.User;
import org.acme.repository.UserRepository;
import org.jboss.logging.Logger;


@ApplicationScoped
public class UserService {
    private static final Logger LOG = Logger.getLogger(UserService.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Inject
    UserRepository userRepository;

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
            LOG.info("User email is : "+ existingUser.getEmail());
            return existingUser;
        }

        // If user is not found, create a new one
        User newUser = new User(email);
        userRepository.persist(newUser);
        LOG.info("User email is : "+ newUser.getEmail());
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

}
