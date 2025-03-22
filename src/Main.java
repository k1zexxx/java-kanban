import interfaces.TaskManager;
import manager.InMemoryTaskManager;
import manager.Managers;
import status.Status;
import tasks.EpicTask;
import tasks.SubTask;
import tasks.Task;

public class Main {

    public static void main(String[] args) {
        TaskManager manager = Managers.getDefault();
        Task task = new Task("Переезд", "собрать вещи", Status.NEW);
        manager.createTask(task);
        task.toString();

        Task task2 = new Task("Продукты", "Купить продукты", Status.NEW);
        manager.createTask(task2);

        EpicTask epicTask1 = new EpicTask("Помыть машину", "Описание", Status.NEW);
        manager.createEpicTask(epicTask1);
        SubTask subTask1 = new SubTask(epicTask1.getId(), "Сесть в машину", "Выехать", Status.NEW);
        SubTask subTask2 = new SubTask(epicTask1.getId(), "Отстоять очередь", "Захеть на мойку", Status.NEW);
        manager.createSubTask(subTask1);
        manager.createSubTask(subTask2);

        EpicTask epicTask2 = new EpicTask("Продукты", "Купить продукты", Status.NEW);
        manager.createEpicTask(epicTask2);
        SubTask subTask3 = new SubTask(epicTask2.getId(), "Молоко", "Деревня", Status.NEW);
        manager.createSubTask(subTask3);

        System.out.println(String.format("Задача %d: %s", task.getId(), task.getName()));
        System.out.println(String.format("Задача %d: %s", task2.getId(), task2.getName()));
        System.out.println(epicTask1.toString());
        System.out.println(epicTask2.toString());
        System.out.println(subTask1.toString());

        subTask1.setStatus(Status.DONE);
        manager.updateSubTask(subTask1);
        System.out.println("После изменения статуса");
        System.out.println(epicTask1.toString());

        System.out.println("После удаления подзадачи");
        manager.deleteSubTaskId(subTask1.getId());
        System.out.println(epicTask1.toString());
        System.out.println("");

        InMemoryTaskManager.printAllTasks(manager);
    }



}
