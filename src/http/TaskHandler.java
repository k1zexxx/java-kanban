package http;

import com.google.gson.*;
import com.sun.net.httpserver.HttpExchange;
import interfaces.TaskManager;
import manager.Managers;
import status.Status;
import tasks.Task;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;

public class TaskHandler extends BaseHttpHandler {
    private Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .create();
    private TaskManager taskManager = Managers.getDefault();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            System.out.println("Система начала обработку");
            String method = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();
            String[] pathParts = path.split("/");
            Task task;
            switch (method) {
                case "POST":
                    String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                    JsonElement jsonElement = JsonParser.parseString(body);
                    JsonObject jsonObject = jsonElement.getAsJsonObject();
                    task = new Task(jsonObject.get("name").getAsString(), jsonObject.get("description").getAsString(), Status.NEW);

                    if (task.getId() == 0) {
                        taskManager.createTask(task);
                        sendText(exchange, body, 201);
                    } else {
                        taskManager.updateTask(task);
                        sendText(exchange, body, 201);
                    }
                    break;

                case "GET":
                    if (pathParts.length == 2) {
                        String tasksJson = gson.toJson(taskManager.getTasks().values());
                        sendText(exchange, tasksJson, 200);
                    } else if (pathParts.length == 3) {
                        int taskId = Integer.parseInt(pathParts[2]);
                        if (taskManager.getTasks().size() < taskId) {
                            sendNotFound(exchange);
                        } else {
                            task = (Task) taskManager.getTasksId(taskId);
                            sendText(exchange, gson.toJson(task), 200);
                        }
                    } else {
                        sendNotFound(exchange);
                    }
                    break;

                case "DELETE":
                    if (pathParts.length == 3) {
                        int taskId = Integer.parseInt(pathParts[2]);
                        taskManager.deleteTaskId(taskId);
                        sendText(exchange, "{\"message\": \"Task deleted\"}", 200);
                    } else {
                        sendNotFound(exchange);
                    }
                    break;

                default:
                    sendNotFound(exchange);
            }
        } catch (Exception e) {
            if (e.getMessage().contains("пересекается")) {
                sendHasInteractions(exchange);
            }
        }
    }
}
