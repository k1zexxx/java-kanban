package manager;

import interfaces.HistoryManager;
import interfaces.TaskManager;
import status.Status;
import tasks.EpicTask;
import tasks.SubTask;
import tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


public class InMemoryTaskManager implements TaskManager  {
    private static HashMap<Integer, Task> tasks = new HashMap<>();
    private static HashMap<Integer, EpicTask> epicTasks = new HashMap<>();
    private static HashMap<Integer, SubTask> subTasks = new HashMap<>();
    private int taskId = 1;
    private static HistoryManager historyManager = Managers.getDefaultHistory();
    private TreeSet<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));


    @Override
    public  HashMap<Integer, Task> getTasks() {
        return tasks;
    }

    @Override
    public HashMap<Integer, EpicTask> getEpicTasks() {
        return epicTasks;
    }

    @Override
    public HashMap<Integer, SubTask> getSubTasks() {
        return subTasks;
    }

    @Override
    public  Task getTasksId(int taskId) {
        historyManager.add(tasks.get(taskId));

        return tasks.get(taskId);
    }

    @Override
    public  EpicTask getEpicTasksId(int taskId) {
        historyManager.add(epicTasks.get(taskId));

        return epicTasks.get(taskId);
    }

    @Override
    public  SubTask getSubTasksId(int taskId) {
        historyManager.add(subTasks.get(taskId));

        return subTasks.get(taskId);
    }

    @Override
    public void deleteTask() {
        tasks.values().forEach(prioritizedTasks::remove);
        tasks.clear();
    }

    @Override
    public void deleteEpicTask() {
        epicTasks.values().forEach(prioritizedTasks::remove);
        subTasks.values().forEach(prioritizedTasks::remove);
        epicTasks.clear();
        subTasks.clear();
    }

    @Override
    public void deleteSubTask() {
        subTasks.values().stream().forEach(prioritizedTasks::remove);
        subTasks.clear();
        epicTasks.values().stream().forEach(epicTask -> {
                    epicTask.getSubTasksId().clear();
                    updateStatusEpicTask(epicTask);
                    updateEpicTimes(epicTask);
                });
    }

    @Override
    public void deleteTaskId(int id) {
        tasks.remove(id);
    }

    @Override
    public void deleteEpicTaskId(int id) {
        epicTasks.remove(id);
    }

    @Override
    public void deleteSubTaskId(int id) {
        SubTask subTask = subTasks.remove(id);
        EpicTask epicTask = epicTasks.get(subTask.getCode());
        epicTask.deleteSubTasId(id);
        updateStatusEpicTask(epicTask);
    }

    @Override
    public void createTask(Task task) {
        if (hasTimeOverlap(task)) {
            throw new IllegalArgumentException("Задача " + task.getName() + " пересекается по времени с существующей задачей");
        }
        task.setId(taskId++);
        tasks.put(task.getId(), task);
        if (task.getStartTime() != null) {
            prioritizedTasks.add(task);
        }
    }

    @Override
    public void createEpicTask(EpicTask epicTask) {
        epicTask.setId(taskId++);
        epicTasks.put(epicTask.getId(), epicTask);


    }

    @Override
    public void createSubTask(SubTask subTask) {
        if (hasTimeOverlap(subTask)) {
            throw new IllegalArgumentException("Задача " + subTask.getName() + " пересекается по времени с существующей задачей");
        }
        subTask.setId(taskId++);
        subTasks.put(subTask.getId(), subTask);
        EpicTask epicTask = epicTasks.get(subTask.getCode());
        epicTask.putSubTasksId(subTask.getId());
        updateStatusEpicTask(epicTask);
        if (subTask.getStartTime() != null) {
            updateEpicTimes(epicTask);
        }
        if (subTask.getStartTime() != null) {
            prioritizedTasks.add(subTask);
        }
    }

    @Override
    public void updateTask(Task task) {
        if (hasTimeOverlap(task)) {
            throw new IllegalArgumentException("Задача " + task.getName() + " пересекается по времени с существующей задачей");
        }
        Task oldTask = tasks.get(task.getId());
        if (oldTask != null) {
            prioritizedTasks.remove(oldTask);
        }
        tasks.put(task.getId(), task);
        if (task.getStartTime() != null) {
            prioritizedTasks.add(task);
        }
    }

    @Override
    public void updateEpicTask(EpicTask epicTask) {
        epicTasks.put(epicTask.getId(), epicTask);
        updateStatusEpicTask(epicTask);
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        if (hasTimeOverlap(subTask)) {
            throw new IllegalArgumentException(String.format("Задача %s пересекается по времени с существующей задачей", subTask.getName()));
        }
        SubTask oldSubTask = subTasks.get(subTask.getId());
        if (oldSubTask != null) {
            prioritizedTasks.remove(oldSubTask);
        }
        subTasks.put(subTask.getId(), subTask);
        EpicTask epicTask = epicTasks.get(subTask.getCode());
        updateStatusEpicTask(epicTask);
        updateEpicTimes(epicTask);
        if (subTask.getStartTime() != null) {
            prioritizedTasks.add(subTask);
        }
    }

    @Override
    public ArrayList<SubTask> getSubTaskList(int epicId) {
        EpicTask epicTask = epicTasks.get(epicId);

        return epicTask.getSubTasksId().stream()
                .map(subTasks::get)
                .collect(Collectors.toCollection(ArrayList::new));

    }

    private void updateStatusEpicTask(EpicTask epicTask) {
        if (epicTask.getSubTasksId().isEmpty()) {
            epicTask.setStatus(Status.NEW);
        }
        List<SubTask> epicSubTasks = epicTask.getSubTasksId().stream()
                .map(subTasks::get)
                .toList();

        boolean statDone = epicSubTasks.stream()
                .allMatch(subTask -> subTask.getStatus() == Status.DONE);

        boolean statNew = epicSubTasks.stream()
                .allMatch(subTask -> subTask.getStatus() == Status.NEW);

        if (statDone) {
            epicTask.setStatus(Status.DONE);
        } else if (statNew) {
            epicTask.setStatus(Status.NEW);
        } else {
            epicTask.setStatus(Status.IN_PROGRESS);
        }
    }

    private void updateEpicTimes(EpicTask epicTask) {

        List<SubTask> allSubTasks = epicTask.getSubTasksId().stream()
                .map(subTasks::get)
                .toList();

        LocalDateTime earliestStart = allSubTasks.stream()
                .map(SubTask::getStartTime)
                .min(LocalDateTime::compareTo)
                .orElse(null);

        LocalDateTime latestEnd = allSubTasks.stream()
                .map(SubTask::getStartTime)
                .max(LocalDateTime::compareTo)
                .orElse(null);

        Duration totalDuration = allSubTasks.stream()
                .map(SubTask::getDuration)
                .reduce(Duration.ZERO, Duration::plus);

        epicTask.setStartTime(earliestStart);
        epicTask.setEndTime(latestEnd);
        epicTask.setDuration(totalDuration);
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }

    private boolean hasTimeOverlap(Task newTask) {
        if (newTask.getStartTime() == null || newTask.getEndTime() == null) {
            return false;
        }

        return prioritizedTasks.stream()
                .filter(task -> task.getId() != newTask.getId())
                .filter(task -> task.getStartTime() != null && task.getEndTime() != null)
                .anyMatch(existingTask -> existingTask.isOverlapping(newTask));
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getTasks();
    }

    public static void printAllTasks(TaskManager<Task> manager) {

        System.out.println("Задачи:");
        manager.getTasks().values().forEach(System.out::println);
        System.out.println("Эпики:");
        manager.getEpicTasks().values().forEach(epic -> {
            System.out.println(epic);
            manager.getSubTaskList(epic.getId()).forEach(task ->
                    System.out.println("--> " + task));
        });
        System.out.println("Подзадачи:");
        manager.getSubTasks().values().forEach(System.out::println);
        System.out.println("Отсортированные задачи по приоритету:");
        manager.getPrioritizedTasks().forEach(task ->
                System.out.println(task.getStartTime() + " - " + task.getName()));

        manager.getEpicTasksId(3);
        System.out.println("История:");
        System.out.println(historyManager.getTasks());
    }

}
