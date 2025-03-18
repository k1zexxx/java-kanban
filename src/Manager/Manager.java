package Manager;

import Status.Status;
import Tasks.EpicTask;
import Tasks.SubTask;
import Tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class Manager {
    private static HashMap<Integer, Task> tasks = new HashMap<>();
    private static HashMap<Integer, EpicTask> epicTasks = new HashMap<>();
    private static HashMap<Integer, SubTask> subTasks = new HashMap<>();
    private int taskId = 1;

    public static HashMap<Integer, Task> getTasks() {
        return tasks;
    }

    public static HashMap<Integer, EpicTask> getEpicTasks() {
        return epicTasks;
    }

    public static HashMap<Integer, SubTask> getSubTasks() {
        return subTasks;
    }

    public  Task getTasksId(int taskId){
        return tasks.get(taskId);
    }

    public  EpicTask getEpicTasksId(int taskId){
        return epicTasks.get(taskId);
    }

    public  SubTask getSubTasksId(int taskId){
        return subTasks.get(taskId);
    }

    public void deleteTask(){
        tasks.clear();
    }

    public void deleteEpicTask(){
        epicTasks.clear();
        subTasks.clear();
    }

    public void deleteSubTask(){
        subTasks.clear();
        for (EpicTask epicTask : epicTasks.values()){
            epicTask.getSubTasksId().clear();
            updateStatusEpicTask(epicTask);
        }
    }

    public void deleteTaskId(int id){
        tasks.remove(id);
    }

    public void deleteEpicTaskId(int id){
        epicTasks.remove(id);
    }

    public void deleteSubTaskId(int id){
        SubTask subTask = subTasks.remove(id);
        EpicTask epicTask = epicTasks.get(subTask.getCode());
        epicTask.deleteSubTasId(id);
        updateStatusEpicTask(epicTask);
    }

    public void createTask(Task task){
        task.setId(taskId++);
        tasks.put(task.getId(), task);

    }

    public void createEpicTask(EpicTask epicTask){
        epicTask.setId(taskId++);
        epicTasks.put(epicTask.getId(), epicTask);


    }

    public void createSubTask(SubTask subTask){
        subTask.setId(taskId++);
        subTasks.put(subTask.getId(), subTask);
        EpicTask epicTask = epicTasks.get(subTask.getCode());
        epicTask.putSubTasksId(subTask.getId());
        updateStatusEpicTask(epicTask);
    }

    public void updateTask(Task task){
        tasks.put(task.getId(), task);
    }

    public void updateEpicTask(EpicTask epicTask){
        epicTasks.put(epicTask.getId(), epicTask);
        updateStatusEpicTask(epicTask);
    }

    public void updateSubTask(SubTask subTask){
        subTasks.put(subTask.getId(), subTask);
        EpicTask epicTask = epicTasks.get(subTask.getCode());
        updateStatusEpicTask(epicTask);
    }


    public ArrayList<SubTask> getSubTaskList(int epicId){
        EpicTask epicTask = epicTasks.get(epicId);
        ArrayList<SubTask> subTaskList = new ArrayList<>();
        for (int subTaskid : epicTask.getSubTasksId()){
            subTaskList.add(subTasks.get(subTaskid));
        }
        return subTaskList;
    }

    private void updateStatusEpicTask(EpicTask epicTask){
        boolean Done = true;
        boolean New = true;

        if (epicTask.getSubTasksId().isEmpty()){
            epicTask.setStatus(Status.NEW);
        }
        ArrayList<Integer> subTaskIds = epicTask.getSubTasksId();

        for (int subTaskId : subTaskIds){
            SubTask subTask = subTasks.get(subTaskId);
            if (subTask.getStatus() != Status.DONE){
                Done = false;
            }
            if (subTask.getStatus() != Status.NEW){
                New = false;
            }
        }

        if (Done){
            epicTask.setStatus(Status.DONE);
        } else if (New) {
            epicTask.setStatus(Status.NEW);
        } else {
            epicTask.setStatus(Status.IN_PROGRESS);
        }
    }


}
