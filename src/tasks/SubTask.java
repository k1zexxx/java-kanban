package tasks;
import status.Status;
public class SubTask extends Task {
    int code;

    public SubTask(int code, String name, String descripton, Status status) {
        super(name, descripton,status);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString(){
        return "Tasks.SubTask {"
                + "name = " + getName() + '\''
                + " descritpion = " + getDescription() + '\''
                + " code = " + getCode() + '\''
                + " status = " + getStatus() + "}";
    }
}
