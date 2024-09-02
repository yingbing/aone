package com.ice;

import com.ice.handlers.DatabaseResource;
import com.ice.handlers.EnvironmentResource;
import com.ice.handlers.MessageResource;
import com.ice.ssh1.WebSocketCommandHandler;
import io.muserver.*;
import io.muserver.handlers.ResourceHandlerBuilder;
import io.muserver.rest.RestHandlerBuilder;

import static io.muserver.ContextHandlerBuilder.context;
import com.fasterxml.jackson.jakarta.rs.json.JacksonJsonProvider;
public class App {

    public static void main(String[] args) {
        JacksonJsonProvider jacksonJsonProvider = new JacksonJsonProvider();

        MuServer server = MuServerBuilder.httpServer()
                .withHttpPort(8080)
                .addHandler(
                        RestHandlerBuilder.restHandler(
                                new MessageResource(),
                                new EnvironmentResource(),
                                new DatabaseResource()
                        )
                        .addCustomWriter(jacksonJsonProvider)
                        .addCustomReader(jacksonJsonProvider)
                )
//                .addHandler(ResourceHandlerBuilder.fileOrClasspath("static", "/"))
                .addHandler(context("/")
                        .addHandler(ResourceHandlerBuilder.classpathHandler("/static"))
                )
                .addHandler(WebSocketCommandHandler.createWebSocketHandler())
                .start();

        System.out.println("Server started at " + server.uri());
    }
}
