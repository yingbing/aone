package com.ice.rundeck;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RundeckJobInvoker {
    private static final String RUNDECK_API_URL = "http://<your-rundeck-server>:4440/api/41";
    private static final String API_TOKEN = "your-api-token";
    private static final String JOB_ID = "your-job-id";

    public static void main(String[] args) {
        try {
            // Start the job
            String executionId = startJob();
            if (executionId != null) {
                // Stream job output
                streamJobOutput(executionId);
            } else {
                System.out.println("Failed to start job.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String startJob() throws Exception {
        URL url = new URL(RUNDECK_API_URL + "/job/" + JOB_ID + "/run");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("X-Rundeck-Auth-Token", API_TOKEN);
        connection.setDoOutput(true);

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Extract execution ID from response
            String responseStr = response.toString();
            return extractExecutionId(responseStr); // Implement this method to extract execution ID
        } else {
            System.out.println("Failed to execute job: HTTP error code " + responseCode);
            return null;
        }
    }

    private static void streamJobOutput(String executionId) throws Exception {
        boolean isRunning = true;
        while (isRunning) {
            URL url = new URL(RUNDECK_API_URL + "/execution/" + executionId + "/output");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("X-Rundeck-Auth-Token", API_TOKEN);

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                    System.out.println(inputLine); // Print each line of output
                }
                in.close();

                // Check job status
                String status = extractJobStatus(response.toString()); // Implement this method to extract job status
                isRunning = "running".equals(status);

                if (isRunning) {
                    Thread.sleep(5000); // Wait for 5 seconds before polling again
                }
            } else {
                System.out.println("Failed to get job output: HTTP error code " + responseCode);
                isRunning = false;
            }
        }
    }

    private static void pollJobStatus(String executionId) throws Exception {
        boolean isRunning = true;
        while (isRunning) {
            URL url = new URL(RUNDECK_API_URL + "/execution/" + executionId);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("X-Rundeck-Auth-Token", API_TOKEN);

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // Check job status
                String status = extractJobStatus(response.toString()); // Implement this method to extract job status
                System.out.println("Job status: " + status);
                isRunning = "running".equals(status);

                if (isRunning) {
                    Thread.sleep(10000); // Wait for 10 seconds before polling again
                }
            } else {
                System.out.println("Failed to get job status: HTTP error code " + responseCode);
                isRunning = false;
            }
        }
    }


    private static String extractExecutionId(String response) {
        // Implement this method to extract execution ID from response
        // Example: parse JSON response and extract "id" field
        return "extracted-execution-id";
    }

    private static String extractJobStatus(String response) {
        // Implement this method to extract job status from response
        // Example: parse JSON response and extract "status" field
        return "extracted-job-status";
    }
}
