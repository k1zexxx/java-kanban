package test;

import interfaces.HistoryManager;
import interfaces.TaskManager;
import manager.Managers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import status.Status;
import tasks.EpicTask;
import tasks.SubTask;
import tasks.Task;

import java.util.List;



public class TaskTest {
    TaskManager manager = Managers.getDefault();
    HistoryManager historyManager = Managers.getDefaultHistory();



    @Test
    public void TestForTask(){
        Task task = new Task("Переезд", "собрать вещи", Status.NEW);
        manager.createTask(task);
        Task task1 = new Task("Помыть машину", "Описание", Status.NEW);
        manager.createTask(task1);
        task1.setId(1);
        Assertions.assertEquals(manager.getTasksId(task.getId()), manager.getTasksId(task1.getId()));
    }

    @Test
    public void TestForEpicTask(){
        EpicTask task = new EpicTask("Переезд", "собрать вещи", Status.NEW);
        manager.createEpicTask(task);
        EpicTask task1 = new EpicTask("Помыть машину", "Описание", Status.NEW);
        manager.createEpicTask(task1);
        task1.setId(1);
        Assertions.assertEquals(task, manager.getEpicTasksId(task.getId()));
    }

    @Test
    public void TestForSubTask(){
        EpicTask task = new EpicTask("Переезд", "собрать вещи", Status.NEW);
        manager.createEpicTask(task);
        SubTask subTask = new SubTask(1, "собрать вещи", "положить куртку",Status.NEW);
        manager.createSubTask(subTask);
        SubTask subTask1 = new SubTask(1, "собрать вещи", "положить штаны",Status.NEW);
        subTask1.setId(2);
        Assertions.assertEquals(manager.getSubTasksId(subTask1.getId()), manager.getSubTasksId(subTask.getId()));
    }

    @Test
    public void TestEpicTaskAddEpicTask(){
        EpicTask task = new EpicTask("Переезд", "собрать вещи", Status.NEW);
        manager.createTask(task);
        Task epicTask = new EpicTask( "Помыть машину", "Описание", Status.NEW);
        Assertions.assertThrows(ClassCastException.class,() -> manager.createSubTask((SubTask) epicTask));
    }

    @Test
    public void TestSubTaskDoEpicTask(){
        Task task = new SubTask(1, "Переезд", "собрать вещи", Status.NEW);
        Assertions.assertThrows(ClassCastException.class,() -> manager.createEpicTask((EpicTask) task));
    }

    @Test
    public void TestMenegersNotNull(){
        Assertions.assertNotNull(manager);
    }

    @Test
    public void TestInMemoryTaskManagerAddTasks(){
        Task task = new Task("Переезд", "собрать вещи", Status.NEW);
        manager.createTask(task);
        Assertions.assertEquals(task, manager.getTasksId(1));
    }

    @Test
    public void TestTaskConflictId(){
        Task task1 = new Task("Переезд", "собрать вещи", Status.NEW);
        manager.createTask(task1);
        Task task2 = new Task("Помыть машину", "Описание", Status.NEW);
        manager.createTask(task2);
        task2.setId(2);
        Assertions.assertEquals(task1, manager.getTasksId(1));
        Assertions.assertEquals(task2, manager.getTasksId(2));
    }

    @Test
    public void add() {
        Task task = new Task("Переезд", "собрать вещи", Status.NEW);
        historyManager.add(task);
        final List<Task> history = historyManager.getHistory();
        Assertions.assertNotNull(history, "История не пустая.");
        Assertions.assertEquals(1, history.size(), "История не пустая.");
    }




}
