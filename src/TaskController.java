import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class TaskController {
    private TaskModel model;
    private TaskView view;
    private Timer timer;
    private LocalDate date = LocalDate.now();
    private TaskSortingStrategy sortingStrategy;

    public TaskController(TaskModel model, TaskView view) {
        this.model = model;
        this.view = view;

        this.view.addAddTaskListener(new AddTaskListener());
        this.view.addDeleteTaskListener(new DeleteTaskListener());
        this.view.addEditTaskListener(new EditTaskListener());

        model.registerObserver(view);
        startTimer();
    }


    private void startTimer() {
        timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                refreshView();
            }
        }, 0, 10000); // Updates every 10 seconds
    }


    private void refreshView() {
        date = date.plusDays(1);
        view.updateDayAndDate();
        checkBirthday();
        updateTaskList();

    }
    private void checkBirthday() {
        String birthday = "2025-02-01"; // Kullanıcının doğum günü
        if (date.toString().equals(birthday)) {
            view.displayBirthdayMessage("Happy Birthday!");
        } else {
            view.clearBirthdayMessage();
        }
    }
    public void setSortingStrategy(TaskSortingStrategy sortingStrategy) {
        this.sortingStrategy = sortingStrategy;
    }

    class AddTaskListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Task task = view.getTaskDetails();
            if (task != null) {
                model.addTask(task);
                updateTaskList();
            }
        }
    }

    class DeleteTaskListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String taskName = view.getSelectedTaskName();
            if (taskName != null) {
                model.deleteTask(taskName);
                updateTaskList();
            }
        }
    }

    class EditTaskListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String oldTaskName = view.getSelectedTaskName();
            Task updatedTask = view.getTaskDetails();
            if (oldTaskName != null && updatedTask != null) {
                model.editTask(oldTaskName, updatedTask);
                updateTaskList();
            }
        }
    }

    private void updateTaskList() {
        List<Task> tasks = model.getTasks();
        if (sortingStrategy != null) {
            tasks = sortingStrategy.sort(tasks);
        }

        StringBuilder taskList = new StringBuilder();

        for (Task task : tasks) {
            taskList.append(task.getName())
                    .append(" - ")
                    .append(task.getDescription())
                    .append(" - ")
                    .append(task.getCategory())
                    .append(" - ")
                    .append(task.getDeadline())
                    .append("\n");
        }

        model.notifyObservers(taskList.toString(), checkDeadlines());
    }
    public String checkDeadlines() {
        List<Task> tasks = model.getTasks();
        LocalDate today = date;
        StringBuilder taskList = new StringBuilder();
        taskList.append("\n");
        for (Task task : tasks) {
            LocalDate deadline = LocalDate.parse(task.getDeadline());
            if (deadline.minusDays(1).isEqual(today)) {

                taskList.append(task.getName()).append("\n");
            }
        }
        return taskList.toString();
    }

}
