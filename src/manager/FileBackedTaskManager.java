package manager;

import interfaces.TaskManager;
import status.Status;
import tasks.EpicTask;
import tasks.Task;
import tasks.SubTask;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;


public class FileBackedTaskManager extends InMemoryTaskManager implements TaskManager {
    private File file;
    private StringBuilder content = new StringBuilder("id,type,name,status,description,epic\n");

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    public FileBackedTaskManager() {

    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager manager = new FileBackedTaskManager();
        try {
            String readFile = Files.readString(file.toPath());
            String[] lines = readFile.split("\n");
            for (int i = 1; i < lines.length; i++) {
                fromString(lines[i]);
            }
        } catch (IOException e) {
            throw new MatchException("Ошибка загрузки данных из файла", e);
        }
        return manager;
    }

    @Override
    public void createTask(Task task) {
        super.createTask(task);
        save(task);
    }

    @Override
    public void createEpicTask(EpicTask epicTask) {
        super.createEpicTask(epicTask);
        save(epicTask);
    }

    @Override
    public void createSubTask(SubTask subtask) {
        super.createSubTask(subtask);
        save(subtask);
    }

    private void save(Task task) {
            content.append(toString(task));
            content.append("\n");
    }

    public void add() {
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(content.toString());
        } catch (IOException ex) {
            throw new MatchException("Ошибка сохранения данных в файл", ex);
        }
    }

    String toString(Task task) {
        if (task instanceof SubTask) {
            SubTask subtask = (SubTask) task;
            return String.format("%d,SUBTASK,%s,%s,%s,%d", subtask.getId(), subtask.getName(), subtask.getStatus(), subtask.getDescription(), subtask.getCode());
        } else if (task instanceof EpicTask) {
            EpicTask epic = (EpicTask) task;
            return String.format("%d,EPIC,%s,%s,%s", epic.getId(), epic.getName(), epic.getStatus(), epic.getDescription());
        } else {
            return String.format("%d,TASK,%s,%s,%s", task.getId(), task.getName(), task.getStatus(), task.getDescription());
        }
    }

    static Task fromString(String value) {
        String[] fields = value.split(",");
        if (fields.length < 5) return null;

        int id = Integer.parseInt(fields[0].trim());
        TaskType type = TaskType.valueOf(fields[1].trim());
        String name = fields[2].trim();
        Status status = Status.valueOf(fields[3].trim());
        String description = fields[4].trim();

        switch (type) {
            case TASK:
                Task task = new Task(name, description, status);
                task.setId(id);
                task.setStatus(status);
                return task;
            case EPIC:
                EpicTask epic = new EpicTask(name, description, status);
                epic.setId(id);
                epic.setStatus(status);
                return epic;
            case SUBTASK:
                if (fields.length > 5 && !fields[5].isEmpty()) {
                    int epicId = Integer.parseInt(fields[5].trim());
                    SubTask subtask = new SubTask(epicId, name, description, status);
                    subtask.setId(id);
                    return subtask;
                }
            default:
                return null;
        }
    }



    enum TaskType {
        TASK,
        EPIC,
        SUBTASK
    }

}
