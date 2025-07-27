package test;

import interfaces.HistoryManager;
import interfaces.TaskManager;
import manager.Managers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import status.Status;
import tasks.Task;

public class HistoryManagerTest {


    @Test
    public void add() {
        TaskManager manager = Managers.getDefault();
        HistoryManager historyManager = Managers.getDefaultHistory();
        Task task1 = new Task("Переезд", "собрать вещи", Status.NEW);
        manager.createTask(task1);
        Task task2 = new Task("Продукты", "купить продукты", Status.NEW);
        manager.createTask(task2);

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task1);
        Assertions.assertEquals(2, historyManager.getTasks().size(), "История не пустая.");
    }
}
