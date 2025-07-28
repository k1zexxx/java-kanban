package manager;

import tasks.Task;

public class Node {
    public Task task;
    public manager.Node next;
    public manager.Node prev;

    public Node(Task task, manager.Node prev, manager.Node next) {
        this.task = task;
        this.next = next;
        this.prev = prev;
        }
}
