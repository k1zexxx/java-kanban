package test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import http.DurationAdapter;
import http.HttpTaskServer;
import http.LocalDateTimeAdapter;
import interfaces.TaskManager;
import manager.InMemoryTaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import status.Status;
import tasks.EpicTask;
import tasks.SubTask;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SubtaskHttpTest {
    private TaskManager manager;
    private HttpTaskServer taskServer;
    private Gson gson;
    private HttpClient client;
    private EpicTask epic;

    @BeforeEach
    void setUp() throws IOException {
        manager = new InMemoryTaskManager();
        taskServer = new HttpTaskServer();
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .create();
        client = HttpClient.newHttpClient();

        epic = new EpicTask("Test Epic", "Testing epic", Status.NEW);
        manager.createEpicTask(epic);

        taskServer.start();
    }

    @AfterEach
    void tearDown() {
        taskServer.stop();
    }

    @Test
    void testAddSubTask() throws IOException, InterruptedException {
        manager.deleteSubTask();
        SubTask subTask = new SubTask(epic.getId(), "Test Subtask", "Testing subtask", Status.NEW);
        String subTaskJson = gson.toJson(subTask);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/subtasks"))
                .POST(HttpRequest.BodyPublishers.ofString(subTaskJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        List<SubTask> subTasksFromManager = manager.getSubTasks().values().stream().toList();

        assertEquals(1, subTasksFromManager.size());
        assertEquals("Test Subtask", subTasksFromManager.get(0).getName());
    }

    @Test
    void testGetSubTask() throws IOException, InterruptedException {
        SubTask subTask = new SubTask(epic.getId(), "Test Subtask", "Testing subtask", Status.NEW);
        manager.createSubTask(subTask);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/subtasks/" + subTask.getId()))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        SubTask responseSubTask = gson.fromJson(response.body(), SubTask.class);
        assertEquals(subTask.getName(), responseSubTask.getName());
    }
}