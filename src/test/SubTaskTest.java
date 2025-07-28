package test;

import interfaces.TaskManager;
import manager.Managers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import status.Status;
import tasks.EpicTask;
import tasks.SubTask;
import tasks.Task;

public class SubTaskTest {
    TaskManager manager = Managers.getDefault();

    @Test
    public void testForSubTask() {
        EpicTask task = new EpicTask("Переезд", "собрать вещи", Status.NEW);
        manager.createEpicTask(task);
        SubTask subTask = new SubTask(1, "собрать вещи", "положить куртку",Status.NEW);
        manager.createSubTask(subTask);
        SubTask subTask1 = new SubTask(1, "собрать вещи", "положить штаны",Status.NEW);
        subTask1.setId(2);
        Assertions.assertEquals(manager.getSubTasksId(subTask1.getId()), manager.getSubTasksId(subTask.getId()));
    }

    @Test
    public void testSubTaskDoEpicTask() {
        Task task = new SubTask(1, "Переезд", "собрать вещи", Status.NEW);
        Assertions.assertThrows(ClassCastException.class,() -> manager.createEpicTask((EpicTask) task));
    }
}
