package com.ice.rundeck;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ice.util.HttpUtil;

public class RundeckJobInvoker {
    private static final String RUNDECK_API_URL = "http://<your-rundeck-server>:4440/api/41";
    private static final String API_TOKEN = "your-api-token";
    private static final String JOB_ID = "your-job-id";

    public static void main(String[] args) {
        try {
            // Define job parameters
            String jobParameters = "{\"argString\": \"-param1 value1 -param2 value2\"}";

            // Start the job with parameters
            String executionId = startJobWithParameters(jobParameters);
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

    private static String startJobWithParameters(String jobParameters) throws Exception {
        String url = RUNDECK_API_URL + "/job/" + JOB_ID + "/run";
        String responseStr = HttpUtil.sendPost(url, jobParameters, API_TOKEN);
        return extractExecutionId(responseStr); // Implement this method to extract execution ID
    }

    private static void streamJobOutput(String executionId) throws Exception {
        boolean isRunning = true;
        ObjectMapper mapper = new ObjectMapper();

        while (isRunning) {
            String url = RUNDECK_API_URL + "/execution/" + executionId + "/output";
            String responseStr = HttpUtil.sendGet(url, API_TOKEN);

            JsonNode rootNode = mapper.readTree(responseStr);
            JsonNode entries = rootNode.path("entries");

            for (JsonNode entry : entries) {
                System.out.println(entry.path("log").asText()); // Print each log entry
            }

            String status = rootNode.path("execState").asText();
            isRunning = "running".equals(status);

            if (isRunning) {
                Thread.sleep(5000); // Wait for 5 seconds before polling again
            }
        }
    }

    private static String extractExecutionId(String response) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(response);
        return rootNode.path("id").asText();
    }
}
