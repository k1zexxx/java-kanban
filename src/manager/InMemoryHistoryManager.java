package manager;

import interfaces.HistoryManager;
import tasks.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    //private List<Task> history= new ArrayList<>();
    private HashMap<Integer, Node> HistoryForTasks = new HashMap<>();
    private Node head;
    private Node tail;
    private int size = 0;


    @Override
    public void add(Task task) {
        //history.add(task);
        remove(task.getId());
        linkLast(task);
    }

    @Override
    public void remove(int id){
        Node node = HistoryForTasks.remove(id);
        if (node != null){
            removeNode(node);
        }
    }

    /*@Override
    public List<Task> getHistory(){
        return history;
    }*/

    private void linkLast(Task task){
        final Node newNode = new Node<>(task, tail, null);

        if (tail == null){
            head = newNode;
        } else {
            tail.next = newNode;
        }

        tail = newNode;
        size++;
        HistoryForTasks.put(task.getId(), newNode);
    }

    public List<Task> getTasks(){
        List<Task> tasks = new ArrayList<>(size);
        Node current = head;

        while (current != null) {
            tasks.add(current.task);
            current = current.next;
        }

        return tasks;
    }

    private void removeNode(Node node){
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

        size--;
    }
}

class Node <T> {

    public Task task;
    public Node<T> next;
    public Node<T> prev;

    public Node(Task task, Node tail, Node head) {
        this.task = task;
        this.next = next;
        this.prev = prev;
    }
}
