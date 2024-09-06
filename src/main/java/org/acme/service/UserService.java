package org.acme.service;

import jakarta.enterprise.context.ApplicationScoped;

import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import org.jboss.logging.Logger;


@ApplicationScoped
public class UserService {
    private static final Logger LOG = Logger.getLogger(UserService.class);

    @PersistenceContext
    private EntityManager entityManager;


    @Transactional
    public void findOrCreateUser(String email) {
        invokeRandomSleepProcedure();
    }
    private void invokeRandomSleepProcedure() {
        // Calling the stored procedure using native query
        var startTime = System.currentTimeMillis();
        LOG.info("Invoking random_sleep stored procedure on thread: " + Thread.currentThread().getName());
        entityManager.createStoredProcedureQuery("random_sleep").execute();
        var took = System.currentTimeMillis() - startTime;
        LOG.info("Completed random_sleep stored procedure. took = {} " + took + " ms");
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
