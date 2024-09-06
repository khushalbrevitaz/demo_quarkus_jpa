package org.acme.controller;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import org.acme.entity.User;
import org.acme.service.UserService;

@Path("/rest")
public class GreetingResource {

    @Inject
    UserService userService;


    @GET
    @Path("/findOrCreate")
    @Produces(MediaType.APPLICATION_JSON)
    public void findOrCreateUser(@QueryParam("email") String email) {
         userService.findOrCreateUser(email);
    }

//    public Uni<User> getUserReactive() {
//        return userService.getUser();
//    }

}
