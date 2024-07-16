package com.ice.handlers;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/message")
public class MessageResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMessage() {
        String json = "{\"message\": \"Hello from Mu Server!\"}";
        return Response.ok(json).build();
    }
}
