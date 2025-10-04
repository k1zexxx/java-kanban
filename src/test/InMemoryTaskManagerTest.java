package test;

import manager.InMemoryTaskManager;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertNotNull;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @Override
    protected InMemoryTaskManager createTaskManager() {
        return new InMemoryTaskManager();
    }

    @Test
    public void testInMemoryTaskManagerAddTasks() {
        var taskManager = createTaskManager();
        assertNotNull(taskManager);
    }
}
