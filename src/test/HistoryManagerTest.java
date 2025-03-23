package test;

import interfaces.HistoryManager;
import interfaces.TaskManager;
import manager.Managers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import status.Status;
import tasks.Task;

import java.util.List;

public class HistoryManagerTest {
    HistoryManager historyManager = Managers.getDefaultHistory();

    @Test
    public void add() {
        Task task = new Task("Переезд", "собрать вещи", Status.NEW);
        historyManager.add(task);
        final List<Task> history = historyManager.getHistory();
        Assertions.assertNotNull(history, "История не пустая.");
        Assertions.assertEquals(1, history.size(), "История не пустая.");
    }
}
