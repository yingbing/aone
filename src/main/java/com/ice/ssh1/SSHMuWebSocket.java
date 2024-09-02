package com.ice.ssh1;

import io.muserver.BaseWebSocket;
import io.muserver.DoneCallback;

public class SSHMuWebSocket extends BaseWebSocket {
    @Override
    public void onText(String message, boolean isLast, DoneCallback onComplete) throws Exception {
        try {
            SSHCommandExecutor executor = new SSHCommandExecutor();
            executor.executeCommandOverWebSocket(session(), message);
        } finally {
            // 确保调用 onComplete 来释放资源
            onComplete.onComplete(null);
        }

        session().sendText("Received " + message, onComplete);
    }
}
