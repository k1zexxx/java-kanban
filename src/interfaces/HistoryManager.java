package interfaces;

import tasks.Task;

import java.util.List;

public interface HistoryManager<T> {
    void add(Task task);
    void remove(int id);
    void getTasks();
    List<Task> getHistory();
}
