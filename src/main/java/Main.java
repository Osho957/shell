import java.io.File;
import java.util.Scanner;
import java.util.Set;

public class Main {

    private static final String PROMPT = "$ ";
    private static final Set<String> BUILTINS = Set.of(
        "cd", "echo", "exit", "help", "history", "pwd", "type",
        "alias", "unalias", "export", "unset", "jobs", "fg", "bg", "kill"
    );

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print(PROMPT);
            if (!scanner.hasNextLine()) break;

            String input = scanner.nextLine();
            if (input.equals("exit")) break;

            if (input.equals("echo") || input.startsWith("echo ")) {
                System.out.println(input.equals("echo") ? "" : input.substring(5));
            } else if (input.startsWith("type ")) {
                String command = input.substring(5);
                if (BUILTINS.contains(command)) {
                    System.out.println(command + " is a shell builtin");
                } else {
                    searchInPath(command);
                }
            } else {
                System.out.println(input + ": command not found");
            }
        }
    }

    private static void searchInPath(String command) {
        String pathEnv = System.getenv("PATH");
        if (pathEnv == null || pathEnv.isEmpty()) {
            System.out.println(command + ": not found");
            return;
        }
        for (String dir : pathEnv.split(File.pathSeparator)) {
            if (dir.isEmpty()) continue;
            File file = new File(dir, command);
            if (file.exists() && file.canExecute()) {
                System.out.println(command + " is " + file.getAbsolutePath());
                return;
            }
        }
        System.out.println(command + ": not found");
    }
}
