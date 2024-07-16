package com.ice.handlers;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.Arrays;
import java.util.List;

@Path("/api/databases")
public class DatabaseResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDatabases() {
        List<String> databases = Arrays.asList("db1", "db2", "db3");
        return Response.ok(databases).build();
    }
}
