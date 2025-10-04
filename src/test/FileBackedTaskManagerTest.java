package test;

import manager.FileBackedTaskManager;
import org.junit.jupiter.api.Test;
import status.Status;
import tasks.Task;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {
    private File testFile;

    @Override
    protected FileBackedTaskManager createTaskManager() {
        try {
            testFile = File.createTempFile("test_tasks", ".csv");
            return new FileBackedTaskManager(testFile);
        } catch (IOException e) {
            throw new RuntimeException("Failed to create test file", e);
        }
    }

    @Test
    public void testSaveAndLoadFromFile() {
        FileBackedTaskManager manager = createTaskManager();

        Task task = new Task("Test Task", "Test Description", Status.NEW);
        manager.createTask(task);
        manager.add(); // Сохраняем в файл

        // Здесь должна быть логика загрузки из файла и проверки
        assertDoesNotThrow(() -> {
            FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(testFile);
            assertNotNull(loadedManager);
        });
    }

    @Test
    public void testFileOperationsExceptionHandling() {
        File invalidFile = new File("/invalid/path/tasks.csv");
        FileBackedTaskManager manager = new FileBackedTaskManager(invalidFile);

        Task task = new Task("Test Task", "Test Description", Status.NEW);
        manager.createTask(task);

        // Проверяем, что исключение перехватывается корректно
        assertThrows(RuntimeException.class, () -> {
            manager.add(); // Попытка сохранить в невалидный путь
        });
    }





}