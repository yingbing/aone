package com.ice.ssh1;

import io.muserver.*;

public class SSHMuWebSocketFactory implements MuWebSocketFactory {
    @Override
    public MuWebSocket create(MuRequest request, Headers responseHeaders) {
        return new SSHMuWebSocket();
    }
}
