package com.ice.ssh1;

import com.jcraft.jsch.*;

import io.muserver.MuWebSocketSession;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class SSHCommandExecutor {

    private static final String HOST = "your-linux-server-ip";
    private static final int PORT = 22;
    private static final String USERNAME = "your-username";
    private static final String PRIVATE_KEY_PATH = "/path/to/your/private_key";

    public void executeCommandOverWebSocket(MuWebSocketSession session, String command) {
        try {
            JSch jsch = new JSch();
            jsch.addIdentity(PRIVATE_KEY_PATH);

            Session jschSession = jsch.getSession(USERNAME, HOST, PORT);
            jschSession.setConfig("StrictHostKeyChecking", "no");
            jschSession.connect();

            Channel channel = jschSession.openChannel("exec");
            ((ChannelExec) channel).setCommand(command);

            channel.setInputStream(null);
            BufferedReader in = new BufferedReader(new InputStreamReader(channel.getInputStream()));
            channel.connect();

            String msg;
            while ((msg = in.readLine()) != null) {
                session.sendText(msg, null);
            }

            channel.disconnect();
            jschSession.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                session.sendText("Error executing command: " + e.getMessage(), null);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}