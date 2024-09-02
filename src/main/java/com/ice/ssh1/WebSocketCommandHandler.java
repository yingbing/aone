package com.ice.ssh1;


import io.muserver.*;

public class WebSocketCommandHandler {

    public static WebSocketHandlerBuilder createWebSocketHandler() {
        return WebSocketHandlerBuilder.webSocketHandler()
                .withPath("/ssh-ws")
                .withWebSocketFactory(new SSHMuWebSocketFactory());
    }
}