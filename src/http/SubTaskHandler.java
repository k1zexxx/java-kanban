package http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import interfaces.TaskManager;
import manager.Managers;
import tasks.SubTask;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;

public class SubTaskHandler extends BaseHttpHandler {

    private Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .create();
    private TaskManager taskManager = Managers.getDefault();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String method = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();
            String[] pathParts = path.split("/");
            SubTask subTask;
            
            switch (method) {
                case "POST":
                    String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                    subTask = gson.fromJson(body, SubTask.class);

                    if (subTask.getId() == 0) {
                        taskManager.createSubTask(subTask);
                        sendText(exchange, gson.toJson(subTask), 201);
                    } else {
                        taskManager.updateSubTask(subTask);
                        sendText(exchange, gson.toJson(subTask), 201);
                    }
                    break;

                case "GET":
                    if (pathParts.length == 2) {
                        String tasksJson = gson.toJson(taskManager.getSubTasks().values());
                        sendText(exchange, tasksJson, 200);
                    } else if (pathParts.length == 3) {
                        int taskId = Integer.parseInt(pathParts[2]);
                        subTask = (SubTask) taskManager.getSubTasksId(taskId);
                        if (subTask == null) {
                            sendNotFound(exchange);
                        }   else {
                            sendText(exchange, gson.toJson(subTask), 200);
                        }
                    } else {
                        sendNotFound(exchange);
                    }
                    break;

                case "DELETE":
                    if (pathParts.length == 3) {
                        int taskId = Integer.parseInt(pathParts[2]);
                        taskManager.deleteSubTaskId(taskId);
                        sendText(exchange, "{\"message\": \"SubTask deleted\"}", 200);
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
