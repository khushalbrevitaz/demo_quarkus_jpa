package org.acme.controller;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import org.acme.entity.User;
import org.acme.service.UserService;
import org.eclipse.microprofile.config.ConfigProvider;

@Path("/rest")
public class GreetingResource {

    @Inject
    UserService userService;


    @GET
    @Path("/findOrCreate")
    @Produces(MediaType.APPLICATION_JSON)
    public User findOrCreateUser(@QueryParam("email") String email) {
//        checkConfigs();
         return userService.findOrCreateUser(email);
    }

    @PostConstruct
    public void checkConfigs() {
//        String hibernateGeneration = ConfigProvider.getConfig().getValue("quarkus.hibernate-orm.database.generation", String.class);
//        int maxPoolSize = ConfigProvider.getConfig().getValue("quarkus.datasource.jdbc.max-size", Integer.class);
//        long connectionTimeout = ConfigProvider.getConfig().getValue("quarkus.datasource.jdbc.connection-timeout", Long.class);
//        int retryAttempts = ConfigProvider.getConfig().getValue("quarkus.datasource.jdbc.acquire-retry-attempts", Integer.class);
//
//        System.out.println("Hibernate Generation: " + hibernateGeneration);
//        System.out.println("Max Pool Size: " + maxPoolSize);
//        System.out.println("Connection Timeout: " + connectionTimeout);
//        System.out.println("Acquire Retry Attempts: " + retryAttempts);
    }

    @GET
    @Path("/spCall")
    @Produces(MediaType.APPLICATION_JSON)
    public void getSPCalled(){
         userService.invokeRandomSleepProcedure();
    }

}
