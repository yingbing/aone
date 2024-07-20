package com.ice.handlers;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.Arrays;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

@Path("/api/databases")
public class DatabaseResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDatabases() {
        List<String> databases = Arrays.asList("db1", "db2", "db3");
        return Response.ok(databases).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response reloadDatabase(Map<String, String> selectedDatabase) {
        System.out.println("Selected Database: " + selectedDatabase);
        Map<String, String> result = new HashMap<>();
        result.put("status", "Database reloaded: " + selectedDatabase);
        return Response.ok(result).build();
    }
}
