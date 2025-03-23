package test;

import interfaces.TaskManager;
import manager.Managers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import status.Status;
import tasks.Task;

public class InMemoryTaskManagerTest {
    TaskManager manager = Managers.getDefault();

    @Test
    public void TestInMemoryTaskManagerAddTasks(){
        Task task = new Task("Переезд", "собрать вещи", Status.NEW);
        manager.createTask(task);
        Assertions.assertEquals(task, manager.getTasksId(1));
    }
}
