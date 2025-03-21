package tasks;

import status.Status;

public class Task {
    private String name;
    private String description;
    private Status status;
    private int id;


    public Task(String name, String description, Status status){
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public Status getStatus() {
        return status;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString(){
        return "Tasks.EpicTask {" + '\''
                + "name = " + getName() + '\''
                + " descritpion = " + getDescription() + '\''
                + " status = " + getStatus() + "} ";
    }

}
