package test;

import interfaces.HistoryManager;
import manager.Managers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import status.Status;
import tasks.Task;

import static org.junit.jupiter.api.Assertions.*;


public class HistoryManagerTest {
    private HistoryManager historyManager;

    @BeforeEach
    public void setUp() {
        historyManager = Managers.getDefaultHistory();
    }

    @Test
    public void testEmptyHistory() {
        // a. Пустая история задач
        assertTrue(historyManager.getTasks().isEmpty());
    }

    @Test
    public void testAddTaskToHistory() {
        Task task = new Task("Test Task", "Description", Status.NEW);
        task.setId(1);

        historyManager.add(task);

        assertEquals(1, historyManager.getTasks().size());
        assertEquals(task, historyManager.getTasks().get(0));
    }

    @Test
    public void testDuplicateTaskInHistory() {
        // b. Дублирование
        Task task = new Task("Test Task", "Description", Status.NEW);
        task.setId(1);

        historyManager.add(task);
        historyManager.add(task); // Дублирование

        assertEquals(1, historyManager.getTasks().size(), "История не должна содержать дубликатов");
    }

    @Test
    public void testRemoveFromBeginning() {
        // c. Удаление из истории: начало
        Task task1 = new Task("Task 1", "Description 1", Status.NEW);
        Task task2 = new Task("Task 2", "Description 2", Status.NEW);
        Task task3 = new Task("Task 3", "Description 3", Status.NEW);
        task1.setId(1);
        task2.setId(2);
        task3.setId(3);

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        historyManager.remove(1); // Удаляем из начала

        assertEquals(2, historyManager.getTasks().size());
        assertFalse(historyManager.getTasks().contains(task1));
    }

    @Test
    public void testRemoveFromMiddle() {
        // c. Удаление из истории: середина
        Task task1 = new Task("Task 1", "Description 1", Status.NEW);
        Task task2 = new Task("Task 2", "Description 2", Status.NEW);
        Task task3 = new Task("Task 3", "Description 3", Status.NEW);
        task1.setId(1);
        task2.setId(2);
        task3.setId(3);

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        historyManager.remove(2); // Удаляем из середины

        assertEquals(2, historyManager.getTasks().size());
        assertFalse(historyManager.getTasks().contains(task2));
    }

    @Test
    public void testRemoveFromEnd() {
        // c. Удаление из истории: конец
        Task task1 = new Task("Task 1", "Description 1", Status.NEW);
        Task task2 = new Task("Task 2", "Description 2", Status.NEW);
        Task task3 = new Task("Task 3", "Description 3", Status.NEW);
        task1.setId(1);
        task2.setId(2);
        task3.setId(3);

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        historyManager.remove(3); // Удаляем с конца

        assertEquals(2, historyManager.getTasks().size());
        assertFalse(historyManager.getTasks().contains(task3));
    }

    @Test
    public void testRemoveNonExistentTask() {
        Task task = new Task("Test Task", "Description", Status.NEW);
        task.setId(1);

        historyManager.add(task);
        historyManager.remove(999); // Удаляем несуществующую задачу

        assertEquals(1, historyManager.getTasks().size());
    }
}
