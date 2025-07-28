package test;

import interfaces.TaskManager;
import manager.Managers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import status.Status;
import tasks.EpicTask;
import tasks.SubTask;
import tasks.Task;

public class EpicTaskTest {
    TaskManager manager = Managers.getDefault();


    @Test
    public void testForEpicTask() {
        EpicTask task = new EpicTask("Переезд", "собрать вещи", Status.NEW);
        manager.createEpicTask(task);
        EpicTask task1 = new EpicTask("Помыть машину", "Описание", Status.NEW);
        manager.createEpicTask(task1);
        task1.setId(1);
        Assertions.assertEquals(task, manager.getEpicTasksId(task.getId()));
    }

    @Test
    public void testEpicTaskAddEpicTask() {
        EpicTask task = new EpicTask("Переезд", "собрать вещи", Status.NEW);
        manager.createTask(task);
        Task epicTask = new EpicTask( "Помыть машину", "Описание", Status.NEW);
        Assertions.assertThrows(ClassCastException.class, () -> manager.createSubTask((SubTask) epicTask));
    }
}
