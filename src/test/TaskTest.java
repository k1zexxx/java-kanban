package test;

import interfaces.HistoryManager;
import interfaces.TaskManager;
import manager.Managers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import status.Status;
import tasks.Task;

public class TaskTest {
    TaskManager manager = Managers.getDefault();
    HistoryManager historyManager = Managers.getDefaultHistory();

    @Test
    public void testForTask(){
        Task task = new Task("Переезд", "собрать вещи", Status.NEW);
        manager.createTask(task);
        Task task1 = new Task("Помыть машину", "Описание", Status.NEW);
        manager.createTask(task1);
        task1.setId(1);
        Assertions.assertEquals(manager.getTasksId(task.getId()), manager.getTasksId(task1.getId()));
    }
    @Test
    public void testMenegersNotNull(){
        Assertions.assertNotNull(manager);
    }
    @Test
    public void testTaskConflictId(){
        Task task1 = new Task("Переезд", "собрать вещи", Status.NEW);
        manager.createTask(task1);
        Task task2 = new Task("Помыть машину", "Описание", Status.NEW);
        manager.createTask(task2);
        task2.setId(2);
        Assertions.assertEquals(task1, manager.getTasksId(1));
        Assertions.assertEquals(task2, manager.getTasksId(2));
    }
}