package com.ice.ssh;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class SSHClient {
    public static void main(String[] args) {
        String host = "your.server.com";
        String user = "yourUsername";
        String privateKey = "/path/to/your/private/key"; // 修改为你的私钥路径
        String command = "yourCommand";

        try {
            JSch jsch = new JSch();
            jsch.addIdentity(privateKey);

            Session session = jsch.getSession(user, host, 22);

            // Avoid asking for key confirmation
            session.setConfig("StrictHostKeyChecking", "no");

            session.connect();

            ChannelExec channel = (ChannelExec) session.openChannel("exec");
            channel.setCommand(command);
            channel.setErrStream(System.err);

            InputStream in = channel.getInputStream();
            channel.connect();

            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            int exitStatus = channel.getExitStatus();
            channel.disconnect();
            session.disconnect();

            System.out.println("Exit status: " + exitStatus);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
