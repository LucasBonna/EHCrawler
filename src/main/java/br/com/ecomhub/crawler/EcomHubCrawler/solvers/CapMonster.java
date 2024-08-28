package br.com.ecomhub.crawler.EcomHubCrawler.solvers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

@Setter
@Component
public class CapMonster {
    private final String apiKey;
    private String websiteKey;
    private String websiteURL;
    private String userAgent;
    private final ObjectMapper mapper = new ObjectMapper();
    private final HttpClient client = HttpClient.newHttpClient();

    public CapMonster(@Value("${capmonster.api.key}") String apiKey) {
        this.apiKey = apiKey;
    }

    public String solveRecaptchaV2() throws Exception {
        long taskId = createTask();
        return getTaskResult(taskId);
    }

    private long createTask() throws Exception {
        System.out.println("Creating task...");
        Map<String, Object> payload = new HashMap<>();
        payload.put("clientKey", this.apiKey);

        Map<String, Object> task = new HashMap<>();
        task.put("type", "RecaptchaV2TaskProxyless");
        task.put("userAgent", this.userAgent);
        task.put("isInvisible", true);
        task.put("websiteURL", this.websiteURL);
        task.put("websiteKey", this.websiteKey);

        payload.put("task", task);

        String json = mapper.writeValueAsString(payload);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.capmonster.cloud/createTask"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JsonNode responseJson = mapper.readTree(response.body());

        if (responseJson.has("errorId") && responseJson.get("errorId").asInt() != 0) {
            throw new Exception("Error creating task: " + responseJson.get("errorDescription").asText());
        }

        System.out.println("Response create Task: " + responseJson);
        return responseJson.get("taskId").asLong();
    }

    private String getTaskResult(long taskId) throws Exception {
        System.out.println("Getting task results...");
        Map<String, Object> payload = new HashMap<>();
        payload.put("clientKey", this.apiKey);
        payload.put("taskId", taskId);

        String json = mapper.writeValueAsString(payload);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.capmonster.cloud/getTaskResult"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        while (true) {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JsonNode responseJson = mapper.readTree(response.body());

            if (responseJson.has("errorId") && responseJson.get("errorId").asInt() != 0) {
                throw new Exception("Error getting task result: " + responseJson.get("errorDescription").asText());
            }

            String status = responseJson.get("status").asText();

            if ("ready".equals(status)) {
                System.out.println("ready task: " + responseJson);
                return responseJson.get("solution").get("gRecaptchaResponse").asText();
            } else if ("processing".equals(status)) {
                System.out.println("Processing task");
                Thread.sleep(2000);
            } else {
                throw new Exception("Unexpected status: " + status);
            }
        }
    }
}
