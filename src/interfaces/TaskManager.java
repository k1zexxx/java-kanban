package interfaces;

import tasks.EpicTask;
import tasks.SubTask;
import tasks.Task;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface TaskManager<T> {
    HashMap<Integer, T> getTasks();

    HashMap<Integer, T> getEpicTasks();

    HashMap<Integer, T> getSubTasks();

    T getTasksId(int taskId);

    T getEpicTasksId(int taskId);

    T getSubTasksId(int taskId);

    void deleteTask();

    void deleteEpicTask();

    void deleteSubTask();

    void deleteTaskId(int id);

    void deleteEpicTaskId(int id);

    void deleteSubTaskId(int id);

    void createTask(Task task);

    void createEpicTask(EpicTask epicTask);

    void createSubTask(SubTask subTask);

    void updateTask(Task task);

    void updateEpicTask(EpicTask epicTask);

    void updateSubTask(SubTask subTask);

    ArrayList<SubTask> getSubTaskList(int epicId);

    static void printAllTasks(TaskManager<Task> manager) {

    }

    List<Task> getHistory();

    List<T> getPrioritizedTasks();
}