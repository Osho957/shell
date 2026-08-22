import java.io.File;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("$ ");
            String input = scanner.nextLine();
            if (input.equals("exit")) {
                break;
            }

            if (input.startsWith("echo ")) {
                System.out.println(input.substring(5));
            } else if (input.startsWith("type ")) {
                String command = input.substring(5);
                if (isBuiltIn(command)){
                  System.out.println(command + " is a shell builtin");
                }else{
                    searchInPath(command);
                }
            } else {
                System.out.println(input + ": command not found");
            }
        }
    }

    private static boolean isBuiltIn(String command){
        String[] builtIns = {"cd", "echo", "exit", "help", "history", "pwd", "type", "alias", "unalias", "export", "unset", "jobs", "fg", "bg", "kill"};
        for (String builtIn : builtIns) {
            if (builtIn.equals(command)) {
                return true;
            }
        }
        return false;
    }

    private static void searchInPath(String command){
        String path  = System.getenv("PATH");
        if (path == null || path.isEmpty()) {
            System.out.println(command + ": not found");
            return;
        }
        for (String dir : path.split(File.pathSeparator)){
            if (dir.isEmpty()) {
                continue;
            }
            File file = new File(dir, command);
            if (file.exists() && file.canExecute()) {
                System.out.println(command+ " is " + file.getPath());
                return;
            }
        }
        System.out.println(command + ": not found");
    }
}
