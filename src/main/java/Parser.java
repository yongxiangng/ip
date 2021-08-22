public class Parser {
    private final TaskList tasks;
    private final Ui ui;

    public Parser(TaskList tasks, Ui ui) {
        this.tasks = tasks;
        this.ui = ui;
    }

    public void parse(String input) throws DukeException {
        Duke.Command command;
        String[] userInput = input.split(" ", 2);

        if (!userInput[0].equals(userInput[0].toLowerCase())) {
            throw new DukeException(
                    String.format("Please input command with lowercase! Did you mean %s?", userInput[0].toLowerCase()));
        }

        try {
            command = Duke.Command.valueOf(userInput[0].toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new DukeException("I'm sorry, but I don't know what that means :-(");
        }

        switch (command) {
        case LIST:
            tasks.print(ui);
            break;
        case DONE:
            setTaskDone(userInput);
            break;
        case TODO:
            addTodo(userInput);
            break;
        case DEADLINE:
            addDeadline(userInput);
            break;
        case EVENT:
            addEvent(userInput);
            break;
        case DELETE:
            deleteTask(userInput);
            break;
        default:
            throw new DukeException("I'm sorry, but I don't know what that means :-(");
        }
    }

    /**
     * Adds the input to the list of tasks and prints out the input.
     *
     * @param input to be added and printed.
     */
    private void addTask(Task input) {
        tasks.add(input);
        ui.print(String.format("Got it. I've added this task:\n  %s\nNow you have %d %s in the list.",
                input, tasks.size(), tasks.size() == 1 ? "task" : "tasks"));
    }

    /**
     * Adds a todo task to the list of tasks.
     *
     * @param userInput given by user.
     */
    private void addTodo(String[] userInput) throws DukeException {
        try {
            addTask(new Todo(userInput[1]));
        } catch (IndexOutOfBoundsException e) {
            throw new DukeException("Todo description cannot be empty");
        }
    }

    /**
     * Adds a deadline task to the list of tasks.
     *
     * @param userInput given by user.
     */
    private void addDeadline(String[] userInput) throws DukeException {
        try {
            String deadlineDescription = userInput[1].split(" /by ")[0];
            String by = userInput[1].split(" /by ")[1];
            addTask(new Deadline(deadlineDescription, by));
        } catch (IndexOutOfBoundsException e) {
            throw new DukeException("Deadline description and time by cannot be empty");
        }
    }

    /**
     * Adds a event task to the list of tasks.
     *
     * @param userInput given by user.
     */
    private void addEvent(String[] userInput) throws DukeException {
        try {
            String eventDescription = userInput[1].split(" /at ")[0];
            String by = userInput[1].split(" /at ")[1];
            addTask(new Event(eventDescription, by));
        } catch (IndexOutOfBoundsException e) {
            throw new DukeException("Event description and time at cannot be empty");
        }
    }

    /**
     * Deletes a task given it's index from the list of tasks.
     *
     * @param userInput given by user.
     */
    private void deleteTask(String[] userInput) throws DukeException {
        try {
            int i = Integer.parseInt(userInput[1]);
            Task task = tasks.get(i - 1);
            tasks.remove(i - 1);
            ui.print(String.format("Noted. I've removed this task:\n  %s\nNow you have %d %s in the list.",
                    task, tasks.size(), tasks.size() == 1
                            ? "task"
                            : "tasks"));
        } catch (IndexOutOfBoundsException | NumberFormatException e) {
            throw new DukeException("Please give a valid number!");
        }
    }

    /**
     * Set i-th task to be done and prints confirmation message.
     *
     * @param userInput given by user.
     */
    private void setTaskDone(String[] userInput) throws DukeException {
        try {
            int i = Integer.parseInt(userInput[1]);
            Task task = tasks.get(i - 1);
            task.setDone();
            ui.print(String.format("Nice! I've marked this task as done:\n  %s", task.toString()));
        } catch (IndexOutOfBoundsException | NumberFormatException e) {
            throw new DukeException("Please give a valid number!");
        }
    }
}
