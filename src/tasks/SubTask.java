package tasks;
import status.Status;

import java.time.Duration;
import java.time.LocalDateTime;

public class SubTask extends Task {
    int code;

    public SubTask(int code, String name, String descripton, Status status, LocalDateTime startTime, Duration duration) {
        super(name, descripton,status, startTime, duration);
        this.code = code;
    }

    public SubTask(int code, String name, String description, Status status) {
        super(name, description, status);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "Tasks.SubTask {"
                + "name = " + getName() + '\''
                + " descritpion = " + getDescription() + '\''
                + " code = " + getCode() + '\''
                + " status = " + getStatus() + "} ";
    }
}