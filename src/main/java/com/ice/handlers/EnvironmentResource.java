package com.ice.handlers;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.Arrays;
import java.util.List;

@Path("/api/environments")
public class EnvironmentResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEnvironments() {
        List<String> environments = Arrays.asList("development", "testing", "production");
        return Response.ok(environments).build();
    }
}
