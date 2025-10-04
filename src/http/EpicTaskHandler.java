package http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import interfaces.TaskManager;
import manager.Managers;
import tasks.EpicTask;
import tasks.SubTask;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class EpicTaskHandler extends BaseHttpHandler {
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
            EpicTask epicTask;

            switch (method) {
                case "POST":
                    String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                    epicTask = gson.fromJson(body, EpicTask.class);
                    if (epicTask.getId() == 0) {
                        taskManager.createEpicTask(epicTask);
                        sendText(exchange, gson.toJson(epicTask), 201);
                    }
                    break;

                case "GET":
                    if (pathParts.length == 2) {
                        String tasksJson = gson.toJson(taskManager.getEpicTasks().values());
                        sendText(exchange, tasksJson, 200);
                    } else if (pathParts.length == 3) {
                        int taskId = Integer.parseInt(pathParts[2]);
                        epicTask = (EpicTask) taskManager.getEpicTasksId(taskId);
                        if (epicTask == null) {
                            sendNotFound(exchange);
                        }   else {
                            sendText(exchange, gson.toJson(epicTask), 200);
                        }
                    } else if (pathParts.length == 4 && "subtasks".equals(pathParts[3])) {
                        int taskId = Integer.parseInt(pathParts[2]);
                        List<SubTask> subTaskList = taskManager.getSubTaskList(taskId);
                        sendText(exchange, gson.toJson(subTaskList), 200);
                    } else {
                        sendNotFound(exchange);
                    }
                    break;

                case "DELETE":
                    if (pathParts.length == 3) {
                        int taskId = Integer.parseInt(pathParts[2]);
                        taskManager.deleteEpicTaskId(taskId);
                        sendText(exchange, "{\"message\": \"EpicTask deleted\"}", 200);
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
