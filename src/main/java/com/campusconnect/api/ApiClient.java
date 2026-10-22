package com.campusconnect.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/** Shared HTTP client for the deployed CampusConnect backend. */
public final class ApiClient {
    private static final String DEFAULT_BASE_URL =
            "https://campusconnect-backend-grn0.onrender.com/api";
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final HttpClient CLIENT = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(15))
            .build();

    private ApiClient() { }

    public static String baseUrl() {
        String configured = System.getProperty("campusconnect.api.baseUrl");
        if (configured == null || configured.isBlank()) {
            configured = DEFAULT_BASE_URL;
        }
        return configured.replaceAll("/+$", "");
    }

    public static JsonNode get(String path) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder(uri(path))
                .timeout(Duration.ofSeconds(30))
                .header("Accept", "application/json")
                .GET().build();
        return send(request);
    }

    public static JsonNode post(String path, Object body)
            throws IOException, InterruptedException {
        String json = MAPPER.writeValueAsString(body);
        HttpRequest request = HttpRequest.newBuilder(uri(path))
                .timeout(Duration.ofSeconds(30))
                .header("Accept", "application/json")
                .header("Content-Type", "application/json; charset=UTF-8")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        return send(request);
    }

    public static JsonNode delete(String path) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder(uri(path))
                .timeout(Duration.ofSeconds(30))
                .header("Accept", "application/json")
                .DELETE().build();
        return send(request);
    }

    private static URI uri(String path) {
        String normalized = path.startsWith("/") ? path : "/" + path;
        return URI.create(baseUrl() + normalized);
    }

    private static JsonNode send(HttpRequest request)
            throws IOException, InterruptedException {
        HttpResponse<String> response = CLIENT.send(
                request, HttpResponse.BodyHandlers.ofString());
        String body = response.body() == null ? "" : response.body().trim();
        if (response.statusCode() < 200 || response.statusCode() >= 300) {
            String message = "CampusConnect API returned HTTP " + response.statusCode();
            if (!body.isBlank()) {
                try {
                    JsonNode error = MAPPER.readTree(body);
                    if (error.hasNonNull("message")) message += ": " + error.get("message").asText();
                    else if (error.hasNonNull("error")) message += ": " + error.get("error").asText();
                } catch (Exception ignored) {
                    message += ": " + body;
                }
            }
            throw new IOException(message);
        }
        if (body.isBlank()) return MAPPER.createObjectNode();
        return MAPPER.readTree(body);
    }

    public static String text(JsonNode node, String... names) {
        if (node == null) return null;
        for (String name : names) {
            JsonNode value = node.get(name);
            if (value != null && !value.isNull()) return value.asText();
        }
        return null;
    }

    public static int integer(JsonNode node, int fallback, String... names) {
        String value = text(node, names);
        if (value == null || value.isBlank()) return fallback;
        try { return Integer.parseInt(value); }
        catch (NumberFormatException ignored) { return fallback; }
    }
}
