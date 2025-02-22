import java.util.List;

public class Main {
    public static void main(String[] args) {
        TaskModel model = new TaskModel();
        TaskView view = new TaskView();
        TaskController controller = new TaskController(model, view);
        controller.setSortingStrategy(new CategorySortingStrategy());

        // Sample Tasks
        model.addTask(new Task("Submit Report", "Submit project report", "Work", "2025-01-30"));
        model.addTask(new Task("Grocery Shopping", "Buy groceries", "Personal", "2025-01-27"));
        model.addTask(new Task("Buyapple", "Buy groceries", "Personal", "2025-01-22"));


    }
}