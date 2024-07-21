package com.ice.ssh;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class SSHClient1 {
    public static void main(String[] args) {
        String host = "your.server.com";
        String user = "yourUsername";
        String command = "yourCommand";

        // Construct the SSH command
        String sshCommand = String.format("ssh %s@%s %s", user, host, command);

        try {
            // Execute the command
            ProcessBuilder builder = new ProcessBuilder("bash", "-c", sshCommand);
            Process process = builder.start();

            // Read the output from the command
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            // Wait for the process to complete and get the exit value
            int exitValue = process.waitFor();
            System.out.println("Exit value: " + exitValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
