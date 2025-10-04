package test;

import interfaces.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import status.Status;
import tasks.EpicTask;
import tasks.SubTask;
import tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {
    protected T taskManager;

    protected abstract T createTaskManager();

    @BeforeEach
    public void setUp() {
        taskManager = createTaskManager();
    }

    @Test
    public void testCreateAndGetTask() {
        Task task = new Task("Test Task", "Test Description", Status.NEW);
        taskManager.createTask(task);

        Task retrievedTask = (Task) taskManager.getTasksId(task.getId());
        assertNotNull(retrievedTask);
        assertEquals(task.getName(), retrievedTask.getName());
        assertEquals(task.getDescription(), retrievedTask.getDescription());
        assertEquals(task.getStatus(), retrievedTask.getStatus());
    }

    @Test
    public void testCreateAndGetEpic() {
        EpicTask epic = new EpicTask("Test Epic", "Test Description", Status.NEW);
        taskManager.createEpicTask(epic);

        EpicTask retrievedEpic = (EpicTask) taskManager.getEpicTasksId(epic.getId());
        assertNotNull(retrievedEpic);
        assertEquals(epic.getName(), retrievedEpic.getName());
    }

    @Test
    public void testCreateAndGetSubTask() {
        EpicTask epic = new EpicTask("Test Epic", "Test Description", Status.NEW);
        taskManager.createEpicTask(epic);

        SubTask subTask = new SubTask(epic.getId(), "Test SubTask", "Test Description", Status.NEW);
        taskManager.createSubTask(subTask);

        SubTask retrievedSubTask = (SubTask) taskManager.getSubTasksId(subTask.getId());
        assertNotNull(retrievedSubTask);
        assertEquals(subTask.getName(), retrievedSubTask.getName());
        assertEquals(epic.getId(), retrievedSubTask.getCode());
    }

    @Test
    public void testDeleteTask() {

        Task task = new Task("Test Task", "Test Description", Status.NEW);
        taskManager.createTask(task);

        taskManager.deleteTaskId(task.getId());

        assertTrue(taskManager.getTasks().isEmpty());
    }

    @Test
    public void testDeleteAllTasks() {
        Task task1 = new Task("Task 1", "Description 1", Status.NEW);
        Task task2 = new Task("Task 2", "Description 2", Status.NEW);
        taskManager.createTask(task1);
        taskManager.createTask(task2);

        taskManager.deleteTask();
        assertTrue(taskManager.getTasks().isEmpty());
    }

    @Test
    public void testUpdateTask() {
        Task task = new Task("Test Task", "Test Description", Status.NEW);
        taskManager.createTask(task);

        task.setStatus(Status.IN_PROGRESS);
        taskManager.updateTask(task);

        Task updatedTask = (Task) taskManager.getTasksId(task.getId());
        assertEquals(Status.IN_PROGRESS, updatedTask.getStatus());
    }

    @Test
    public void testGetPrioritizedTasks() {
        LocalDateTime now = LocalDateTime.now();

        Task task1 = new Task("Task 1", "Description 1", Status.NEW,
                now.plusHours(2), Duration.ofHours(1));
        Task task2 = new Task("Task 2", "Description 2", Status.NEW,
                now.plusHours(1), Duration.ofHours(1));

        taskManager.createTask(task1);
        taskManager.createTask(task2);

        var prioritized = taskManager.getPrioritizedTasks();
        assertEquals(2, prioritized.size());
        assertEquals(task2, prioritized.get(0));
        taskManager.deleteTask();// Должен быть первым из-за более раннего времени
    }

    @Test
    public void testTimeOverlapDetection() {
        LocalDateTime baseTime = LocalDateTime.now();

        Task task1 = new Task("Task 1", "Description 1", Status.NEW,
                baseTime, Duration.ofHours(2));
        Task task2 = new Task("Task 2", "Description 2", Status.NEW,
                baseTime.plusHours(1), Duration.ofHours(1)); // Пересекается

        taskManager.createTask(task1);

        assertThrows(IllegalArgumentException.class, () -> {
            taskManager.createTask(task2);
        });
        taskManager.deleteTask();
    }
}
