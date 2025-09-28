package tasks;

import status.Status;

import java.time.Duration;
import java.util.ArrayList;
import java.time.LocalDateTime;


public class EpicTask extends Task {

    private ArrayList<Integer> subTasksId;


    public EpicTask(String name, String description, Status status, LocalDateTime startTime, Duration duration) {
        super(name, description,status, startTime, duration);
        this.subTasksId = new ArrayList<>();
    }

    public EpicTask(String name, String description, Status status) {
        super(name, description, status);
        this.subTasksId = new ArrayList<>();
    }

    public ArrayList<Integer> getSubTasksId() {
        return subTasksId;
    }

    public void putSubTasksId(int subTaskId) {
        subTasksId.add(subTaskId);
    }

    public void deleteSubTasId(int subTaskId) {
        subTasksId.remove((Integer) subTaskId);
    }


    @Override
    public String toString() {
        return "Tasks.EpicTask {" + '\''
                + "name = " + getName() + '\''
                + " descritpion = " + getDescription() + '\''
                + " subTaskId = " + getSubTasksId() + '\''
                + " status = " + getStatus() + "} ";
    }
}
