package manager;

import interfaces.HistoryManager;
import interfaces.TaskManager;
import status.Status;
import tasks.EpicTask;
import tasks.SubTask;
import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;


public class InMemoryTaskManager implements TaskManager  {
    private static HashMap<Integer, Task> tasks = new HashMap<>();
    private static HashMap<Integer, EpicTask> epicTasks = new HashMap<>();
    private static HashMap<Integer, SubTask> subTasks = new HashMap<>();
    private int taskId = 1;
    private static HistoryManager historyManager = Managers.getDefaultHistory();



    @Override
    public  HashMap<Integer, Task> getTasks() {
        return tasks;
    }

    @Override
    public HashMap<Integer, EpicTask> getEpicTasks() {
        return epicTasks;
    }

    @Override
    public HashMap<Integer, SubTask> getSubTasks() {
        return subTasks;
    }

    @Override
    public  Task getTasksId(int taskId){
        historyManager.add(tasks.get(taskId));

        return tasks.get(taskId);
    }

    @Override
    public  EpicTask getEpicTasksId(int taskId){
        historyManager.add(epicTasks.get(taskId));

        return epicTasks.get(taskId);
    }

    @Override
    public  SubTask getSubTasksId(int taskId){
        historyManager.add(subTasks.get(taskId));

        return subTasks.get(taskId);
    }

    @Override
    public void deleteTask(){
        tasks.clear();
    }

    @Override
    public void deleteEpicTask(){
        epicTasks.clear();
        subTasks.clear();
    }

    @Override
    public void deleteSubTask(){
        subTasks.clear();
        for (EpicTask epicTask : epicTasks.values()){
            epicTask.getSubTasksId().clear();
            updateStatusEpicTask(epicTask);
        }
    }

    @Override
    public void deleteTaskId(int id){
        tasks.remove(id);
    }

    @Override
    public void deleteEpicTaskId(int id){
        epicTasks.remove(id);
    }

    @Override
    public void deleteSubTaskId(int id){
        SubTask subTask = subTasks.remove(id);
        EpicTask epicTask = epicTasks.get(subTask.getCode());
        epicTask.deleteSubTasId(id);
        updateStatusEpicTask(epicTask);
    }

    @Override
    public void createTask(Task task){
        task.setId(taskId++);
        tasks.put(task.getId(), task);

    }

    @Override
    public void createEpicTask(EpicTask epicTask){
        epicTask.setId(taskId++);
        epicTasks.put(epicTask.getId(), epicTask);


    }

    @Override
    public void createSubTask(SubTask subTask){
        subTask.setId(taskId++);
        subTasks.put(subTask.getId(), subTask);
        EpicTask epicTask = epicTasks.get(subTask.getCode());
        epicTask.putSubTasksId(subTask.getId());
        updateStatusEpicTask(epicTask);
    }

    @Override
    public void updateTask(Task task){
        tasks.put(task.getId(), task);
    }

    @Override
    public void updateEpicTask(EpicTask epicTask){
        epicTasks.put(epicTask.getId(), epicTask);
        updateStatusEpicTask(epicTask);
    }

    @Override
    public void updateSubTask(SubTask subTask){
        subTasks.put(subTask.getId(), subTask);
        EpicTask epicTask = epicTasks.get(subTask.getCode());
        updateStatusEpicTask(epicTask);
    }

    @Override
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


    public static void printAllTasks(TaskManager<Task> manager) {

        System.out.println("Задачи:");
        for (Task task : manager.getTasks().values()) {
            System.out.println(task.toString());
        }
        System.out.println("Эпики:");
        for (Task epic : manager.getEpicTasks().values()) {
            System.out.println(epic);

            for (Task task : manager.getSubTaskList(epic.getId())) {
                System.out.println("--> " + task);
            }
        }
        System.out.println("Подзадачи:");
        for (Task subtask : manager.getSubTasks().values()) {
            System.out.println(subtask);
        }
        manager.getEpicTasksId(3);

        System.out.println("История:");
        System.out.println(historyManager.getHistory());
    }

}
