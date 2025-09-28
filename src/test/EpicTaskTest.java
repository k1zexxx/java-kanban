package test;

import interfaces.TaskManager;
import manager.Managers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import status.Status;
import tasks.EpicTask;
import tasks.SubTask;
import tasks.Task;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EpicTaskTest {
    private TaskManager taskManager;

    @BeforeEach
    public void setUp() {
        taskManager = Managers.getDefault();
    }

    @Test
    public void testEpicStatusAllNew() {
        // a. Все подзадачи со статусом NEW
        EpicTask epic = new EpicTask("Epic", "Description", Status.NEW);
        taskManager.createEpicTask(epic);
        System.out.println(taskManager.getEpicTasks());

        SubTask subTask1 = new SubTask(epic.getId(), "SubTask 1", "Description 1", Status.NEW);

        taskManager.createSubTask(subTask1);



        assertEquals(Status.NEW, epic.getStatus());
    }

    @Test
    public void testEpicStatusAllDone() {
        // b. Все подзадачи со статусом DONE
        EpicTask epic = new EpicTask("Epic", "Description", Status.NEW);
        taskManager.createEpicTask(epic);

        SubTask subTask1 = new SubTask(epic.getId(), "SubTask 1", "Description 1", Status.DONE);
        SubTask subTask2 = new SubTask(epic.getId(), "SubTask 2", "Description 2", Status.DONE);
        taskManager.createSubTask(subTask1);
        taskManager.createSubTask(subTask2);

        assertEquals(Status.DONE, epic.getStatus());
    }

    @Test
    public void testEpicStatusNewAndDone() {
        // c. Подзадачи со статусами NEW и DONE
        EpicTask epic = new EpicTask("Epic", "Description", Status.NEW);
        taskManager.createEpicTask(epic);

        SubTask subTask1 = new SubTask(epic.getId(), "SubTask 1", "Description 1", Status.NEW);
        SubTask subTask2 = new SubTask(epic.getId(), "SubTask 2", "Description 2", Status.DONE);
        taskManager.createSubTask(subTask1);
        taskManager.createSubTask(subTask2);

        assertEquals(Status.IN_PROGRESS, epic.getStatus());
    }

    @Test
    public void testEpicStatusInProgress() {
        // d. Подзадачи со статусом IN_PROGRESS
        EpicTask epic = new EpicTask("Epic", "Description", Status.NEW);
        taskManager.createEpicTask(epic);

        SubTask subTask1 = new SubTask(epic.getId(), "SubTask 1", "Description 1", Status.IN_PROGRESS);
        SubTask subTask2 = new SubTask(epic.getId(), "SubTask 2", "Description 2", Status.IN_PROGRESS);
        taskManager.createSubTask(subTask1);
        taskManager.createSubTask(subTask2);

        assertEquals(Status.IN_PROGRESS, epic.getStatus());
    }

    @Test
    public void testEpicStatusMixed() {
        EpicTask epic = new EpicTask("Epic", "Description", Status.NEW);
        taskManager.createEpicTask(epic);

        SubTask subTask1 = new SubTask(epic.getId(), "SubTask 1", "Description 1", Status.NEW);
        SubTask subTask2 = new SubTask(epic.getId(), "SubTask 2", "Description 2", Status.IN_PROGRESS);
        SubTask subTask3 = new SubTask(epic.getId(), "SubTask 3", "Description 3", Status.DONE);
        taskManager.createSubTask(subTask1);
        taskManager.createSubTask(subTask2);
        taskManager.createSubTask(subTask3);

        assertEquals(Status.IN_PROGRESS, epic.getStatus());
    }

    @Test
    public void testEpicStatusEmpty() {
        EpicTask epic = new EpicTask("Epic", "Description", Status.NEW);
        taskManager.createEpicTask(epic);

        // Эпик без подзадач должен иметь статус NEW
        assertEquals(Status.NEW, epic.getStatus());
    }
}