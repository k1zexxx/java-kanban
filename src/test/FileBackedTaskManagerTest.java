package test;

import manager.FileBackedTaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import status.Status;
import tasks.EpicTask;
import tasks.SubTask;
import tasks.Task;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest {
    File testFile = File.createTempFile("test_tasks", ".csv");
    FileBackedTaskManager manager = new FileBackedTaskManager(testFile);

    FileBackedTaskManagerTest() throws IOException {
    }

    @Test
    public void testForTaskManager() {
        EpicTask task = new EpicTask("Переезд", "собрать вещи", Status.NEW);
        manager.createEpicTask(task);
        manager.createTask(new Task("Покупки", "купить арбуз", Status.NEW));
        manager.createTask(new Task("Помыть машину", "Описание", Status.NEW));
        manager.createSubTask(new SubTask(task.getId(), "собрать вещи", "положить куртку", Status.NEW));
        manager.add();
        FileBackedTaskManager manager1 = FileBackedTaskManager.loadFromFile(testFile);
        Assertions.assertEquals(manager.getTasks().size(), manager1.getTasks().size());
    }





}