package manager;

import interfaces.HistoryManager;
import tasks.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private HashMap<Integer, Node> historyForTasks = new HashMap<>();
    private Node head;
    private Node tail;


    @Override
    public void add(Task task) {
        remove(task.getId());
        linkLast(task);
    }

    @Override
    public void remove(int id) {
        Node node = historyForTasks.remove(id);

        if (node != null){
            removeNode(node);
        }
    }

    private void linkLast(Task task) {
        final Node oldTail = tail;
        final Node newNode = new Node(task, tail, null);

        if (oldTail == null) {
            head = newNode;
        } else {
            oldTail.next = newNode;
        }

        tail = newNode;
        historyForTasks.put(task.getId(), newNode);
    }

    @Override
    public List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>();
        Node current = head;
        while (current != null) {
            tasks.add(current.task);
            System.out.println(tasks);
            current = current.next;
        }
        return tasks;
    }

    private void removeNode(Node node) {

        if (node.prev != null){
            node.prev.next = node.next;
        } else {
            head = node.next;
        }

        if (node.next != null) {
            node.next.prev = node.prev;
        } else {
            tail = node.prev;
        }
    }
}

class Node {

    public Task task;
    public Node next;
    public Node prev;

    public Node(Task task, Node prev, Node next) {
        this.task = task;
        this.next = next;
        this.prev = prev;
    }
}
