package tasks;

import status.Status;

import java.time.Duration;
import java.time.LocalDateTime;

public class Task {
    private String name;
    private String description;
    private Status status;
    private int id;
    private Duration duration;
    private LocalDateTime startTime;
    private LocalDateTime endTime;


    public Task(String name, String description, Status status, LocalDateTime startTime, Duration duration) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.startTime = startTime;
        this.duration = duration;
    }

    public Task(String name, String description, Status status) {
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

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public Duration getDuration() {
        return duration;
    }

    public LocalDateTime getEndTime() {

        return startTime.plus(duration);
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public boolean isOverlapping(Task other) {
        if (this.startTime == null || other.startTime == null ||
                this.getEndTime() == null || other.getEndTime() == null) {
            return false;
        }
        return this.startTime.isBefore(other.getEndTime()) &&
                other.startTime.isBefore(this.getEndTime());
    }

    @Override
    public String toString() {
        return "Tasks.EpicTask {" + '\''
                + "name = " + getName() + '\''
                + " descritpion = " + getDescription() + '\''
                + " status = " + getStatus() + "} ";
    }

}
