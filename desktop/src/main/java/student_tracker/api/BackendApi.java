package student_tracker.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import student_tracker.model.*;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class BackendApi {
    private final HttpClient client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(20)).build();
    private final ObjectMapper mapper;
    private final String base = System.getProperty("student.tracker.baseUrl", "http://localhost:8080");
    private final String apiBase = base + "/api";
    private final String authBase = base + "/auth";

    public BackendApi(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public LoginResponse login(String username, String password) throws Exception {
        JsonNode body = mapper.createObjectNode().put("username", username).put("password", password);
        JsonNode node = sendJson(authBase + "/login", "POST", body, null);
        return new LoginResponse(node.path("accessToken").asText());
    }

    public void signup(String name, String username, int studentType, String password) throws Exception {
        JsonNode body = mapper.createObjectNode()
            .put("name", name).put("username", username).put("studentType", studentType).put("password", password);
        sendJson(authBase + "/signup", "POST", body, null);
    }

    public UserProfile getProfile(String token) throws Exception {
        JsonNode node = sendJson(apiBase + "/users/profile", "GET", null, token);
        Long groupId = node.path("groupId").isNull() || node.path("groupId").isMissingNode() ? null : node.path("groupId").asLong();
        return new UserProfile(
            node.path("id").asLong(),
            node.path("name").asText(""),
            node.path("username").asText(""),
            node.path("email").asText(""),
            node.path("studentType").asText(""),
            groupId
        );
    }

    public void updateProfile(String token, String name) throws Exception {
        JsonNode body = mapper.createObjectNode().put("name", name);
        sendJson(apiBase + "/users/profile", "PUT", body, token);
    }

    public void requestEmailChange(String token, String email) throws Exception {
        sendText(apiBase + "/users/request-email-change?newEmail=" + enc(email), "PUT", null, token);
    }

    public void changePassword(String token, String currentPassword, String newPassword) throws Exception {
        JsonNode body = mapper.createObjectNode()
            .put("currentPassword", currentPassword)
            .put("newPassword", newPassword)
            .put("confirmPassword", newPassword);
        sendJson(apiBase + "/users/change-password", "PUT", body, token);
    }

    public void forgotPassword(String email) throws Exception {
        sendText(apiBase + "/users/forgot-password?email=" + enc(email), "POST", "", null);
    }

    public void deleteAccount(String token, String password) throws Exception {
        String url = apiBase + "/users/delete";
        if (password != null && !password.isBlank()) {
            url += "?password=" + enc(password);
        }
        sendText(url, "DELETE", null, token);
    }

    public List<Subject> getSubjects(String token) throws Exception {
        JsonNode node = sendJson(apiBase + "/training-subjects", "GET", null, token);
        List<Subject> out = new ArrayList<>();
        if (node.isArray()) {
            for (JsonNode s : node) {
                out.add(new Subject(s.path("id").asLong(), s.path("title").asText(""), s.path("description").asText(""), s.path("photoUrl").asText("")));
            }
        }
        return out;
    }

    public void createSubject(String token, SubjectPayload payload) throws Exception {
        JsonNode dto = mapper.createObjectNode().put("title", payload.title()).put("description", payload.description());
        sendMultipart(apiBase + "/training-subjects", token, Map.of("data", mapper.writeValueAsString(dto)), "photo", payload.photoPath());
    }

    public void deleteSubject(String token, long subjectId) throws Exception {
        sendText(apiBase + "/training-subjects/" + subjectId, "DELETE", null, token);
    }

    public List<Material> getMaterials(String token, long subjectId) throws Exception {
        JsonNode node = sendJson(apiBase + "/educational-materials/subject/" + subjectId, "GET", null, token);
        List<Material> out = new ArrayList<>();
        if (node.isArray()) {
            for (JsonNode s : node) {
                out.add(new Material(
                    s.path("id").asLong(),
                    s.path("title").asText(""),
                    s.path("description").asText(""),
                    s.path("originalFileName").asText(""),
                    s.path("filePath").asText("")
                ));
            }
        }
        return out;
    }

    public void createMaterial(String token, long subjectId, MaterialPayload payload) throws Exception {
        JsonNode dto = mapper.createObjectNode()
            .put("trainingSubjectId", subjectId)
            .put("title", payload.title())
            .put("description", payload.description());
        sendMultipart(apiBase + "/educational-materials", token, Map.of("data", mapper.writeValueAsString(dto)), "file", payload.filePath());
    }

    public void updateMaterial(String token, long materialId, long subjectId, MaterialPayload payload) throws Exception {
        JsonNode dto = mapper.createObjectNode()
            .put("trainingSubjectId", subjectId)
            .put("title", payload.title())
            .put("description", payload.description());
        sendMultipart(apiBase + "/educational-materials/" + materialId, token, Map.of("data", mapper.writeValueAsString(dto)), "file", payload.filePath());
    }

    public void deleteMaterial(String token, long materialId) throws Exception {
        sendText(apiBase + "/educational-materials/" + materialId, "DELETE", null, token);
    }

    public Group getMyGroup(String token) throws Exception {
        JsonNode node = sendJson(apiBase + "/groups/my-group", "GET", null, token);
        List<GroupMember> members = new ArrayList<>();
        JsonNode membersNode = node.path("members");
        if (membersNode.isArray()) {
            for (JsonNode m : membersNode) {
                members.add(new GroupMember(m.path("id").asLong(), m.path("name").asText(""), m.path("username").asText(""), m.path("studentType").asText("")));
            }
        }
        return new Group(node.path("name").asText(""), members);
    }

    public boolean hasGroup(String token) throws Exception {
        try {
            Group group = getMyGroup(token);
            return group != null && group.name() != null && !group.name().isBlank();
        } catch (Exception ex) {
            String message = ex.getMessage() == null ? "" : ex.getMessage();
            if (message.contains("HTTP 404")) {
                return false;
            }
            throw ex;
        }
    }

    public void createGroup(String token, String groupName) throws Exception {
        JsonNode body = mapper.createObjectNode().put("name", groupName);
        sendText(apiBase + "/groups", "POST", mapper.writeValueAsString(body), token, "application/json");
    }

    public void addUserToGroup(String token, String username) throws Exception {
        sendText(apiBase + "/groups/add-student/" + enc(username), "PUT", null, token);
    }

    public void deleteUserFromGroup(String token, long userId) throws Exception {
        sendText(apiBase + "/groups/delete-student/" + userId, "DELETE", null, token);
    }

    public void deleteGroup(String token) throws Exception {
        sendText(apiBase + "/groups", "DELETE", null, token);
    }

    public void downloadMaterial(String token, String url, Path target) throws Exception {
        HttpRequest request = HttpRequest.newBuilder(URI.create(url))
            .header("Authorization", "Bearer " + token)
            .GET()
            .build();
        HttpResponse<byte[]> response = client.send(request, HttpResponse.BodyHandlers.ofByteArray());
        if (response.statusCode() < 200 || response.statusCode() >= 300) {
            throw new IOException("HTTP " + response.statusCode());
        }
        Files.write(target, response.body());
    }

    private JsonNode sendJson(String url, String method, JsonNode body, String token) throws Exception {
        HttpRequest.Builder builder = HttpRequest.newBuilder(URI.create(url)).header("Accept", "application/json");
        if (token != null && !token.isBlank()) builder.header("Authorization", "Bearer " + token);
        switch (method) {
            case "POST" -> builder.POST(HttpRequest.BodyPublishers.ofString(body == null ? "" : mapper.writeValueAsString(body)))
                .header("Content-Type", "application/json");
            case "PUT" -> builder.PUT(HttpRequest.BodyPublishers.ofString(body == null ? "" : mapper.writeValueAsString(body)))
                .header("Content-Type", "application/json");
            case "DELETE" -> builder.DELETE();
            default -> builder.GET();
        }
        HttpResponse<String> response = client.send(builder.build(), HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() < 200 || response.statusCode() >= 300) {
            throw new IOException("HTTP " + response.statusCode() + ": " + response.body());
        }
        if (response.body() == null || response.body().isBlank()) return mapper.createObjectNode();
        return mapper.readTree(response.body());
    }

    private void sendText(String url, String method, String body, String token) throws Exception {
        HttpRequest.Builder builder = HttpRequest.newBuilder(URI.create(url)).header("Accept", "application/json");
        if (token != null && !token.isBlank()) builder.header("Authorization", "Bearer " + token);
        switch (method) {
            case "POST" -> builder.POST(HttpRequest.BodyPublishers.ofString(body == null ? "" : body));
            case "PUT" -> builder.PUT(HttpRequest.BodyPublishers.ofString(body == null ? "" : body));
            case "DELETE" -> builder.DELETE();
            default -> builder.GET();
        }
        HttpResponse<String> response = client.send(builder.build(), HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() < 200 || response.statusCode() >= 300) {
            throw new IOException("HTTP " + response.statusCode() + ": " + response.body());
        }
    }

    private void sendText(String url, String method, String body, String token, String contentType) throws Exception {
        HttpRequest.Builder builder = HttpRequest.newBuilder(URI.create(url)).header("Accept", "application/json");
        if (token != null && !token.isBlank()) builder.header("Authorization", "Bearer " + token);
        if (contentType != null && !contentType.isBlank()) builder.header("Content-Type", contentType);
        switch (method) {
            case "POST" -> builder.POST(HttpRequest.BodyPublishers.ofString(body == null ? "" : body));
            case "PUT" -> builder.PUT(HttpRequest.BodyPublishers.ofString(body == null ? "" : body));
            case "DELETE" -> builder.DELETE();
            default -> builder.GET();
        }
        HttpResponse<String> response = client.send(builder.build(), HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() < 200 || response.statusCode() >= 300) {
            throw new IOException("HTTP " + response.statusCode() + ": " + response.body());
        }
    }

    private void sendMultipart(String url, String token, Map<String, String> textParts, String filePartName, Path filePath) throws Exception {
        String boundary = "----JavaFxBoundary" + UUID.randomUUID();
        List<byte[]> chunks = new ArrayList<>();
        for (Map.Entry<String, String> entry : textParts.entrySet()) {
            chunks.add(("--" + boundary + "\r\n").getBytes(StandardCharsets.UTF_8));
            chunks.add(("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"\r\n\r\n").getBytes(StandardCharsets.UTF_8));
            chunks.add((entry.getValue() + "\r\n").getBytes(StandardCharsets.UTF_8));
        }
        if (filePath != null) {
            chunks.add(("--" + boundary + "\r\n").getBytes(StandardCharsets.UTF_8));
            chunks.add(("Content-Disposition: form-data; name=\"" + filePartName + "\"; filename=\"" + filePath.getFileName() + "\"\r\n").getBytes(StandardCharsets.UTF_8));
            chunks.add(("Content-Type: application/octet-stream\r\n\r\n").getBytes(StandardCharsets.UTF_8));
            chunks.add(Files.readAllBytes(filePath));
            chunks.add("\r\n".getBytes(StandardCharsets.UTF_8));
        }
        chunks.add(("--" + boundary + "--\r\n").getBytes(StandardCharsets.UTF_8));

        int total = chunks.stream().mapToInt(a -> a.length).sum();
        byte[] body = new byte[total];
        int offset = 0;
        for (byte[] chunk : chunks) {
            System.arraycopy(chunk, 0, body, offset, chunk.length);
            offset += chunk.length;
        }

        HttpRequest request = HttpRequest.newBuilder(URI.create(url))
            .header("Authorization", "Bearer " + token)
            .header("Content-Type", "multipart/form-data; boundary=" + boundary)
            .POST(HttpRequest.BodyPublishers.ofByteArray(body))
            .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() < 200 || response.statusCode() >= 300) {
            throw new IOException("HTTP " + response.statusCode() + ": " + response.body());
        }
    }

    private String enc(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}
