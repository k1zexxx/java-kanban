package manager;

import interfaces.HistoryManager;
import tasks.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private List<Task> tasks = new ArrayList<>();
    private HashMap<Integer, Node> historyForTasks = new HashMap<>();
    private Node head;
    private Node tail;
    private int size = 0;


    @Override
    public void add(Task task) {
        //if (task == null) return;

        remove(task.getId()-1);

        linkLast(task);
    }

    @Override
    public void remove(int id){
        Node node = historyForTasks.remove(id);

        if (node != null){
            removeNode(node);
        }
    }

   @Override
    public List<Task> getHistory() {
        return tasks;
    }

    private void linkLast(Task task){
        final Node newNode = new Node(task, tail, null);

        if (tail == null){
            head = newNode;
        } else {
            tail.next = newNode;
        }

        tail = newNode;
        size++;
        historyForTasks.put(task.getId(), newNode);
        getTasks();
    }

    @Override
    public void getTasks(){
       List<Task> tasks = new ArrayList<>();
        Node current = head;
        //System.out.println(historyForTasks.get(1));
        while (current != null) {


            //System.out.println(current.task);
            tasks.add(current.task);
            System.out.println(tasks);
            current = current.prev;
        }

    }

    private void removeNode(Node node){

        if (node.prev != null){
            node.prev.next = node.next;
        } else {
            head = node.next;
            //size--;
        }

        if (node.next != null) {
            node.next.prev = node.prev;
        } else {
            tail = node.prev;
            //size--;
        }
        size --;
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
