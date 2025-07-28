package interfaces;

import tasks.Task;

import java.util.List;

public interface HistoryManager<T> {
    void add(Task task);

    void remove(int id);

    List<T> getTasks();
}
