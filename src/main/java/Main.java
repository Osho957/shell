import java.io.*;
import java.util.*;

public class Main {

    private static final String PROMPT = "$ ";
    private static final Set<String> BUILTINS = Set.of(
            "cd", "echo", "exit", "help", "history", "pwd", "type",
            "alias", "unalias", "export", "unset", "jobs", "fg", "bg", "kill");

    private static File currentDir;
    static {
        try {
            currentDir = new File(System.getProperty("user.dir")).getCanonicalFile();
        } catch (IOException e) {
            currentDir = new File(System.getProperty("user.dir"));
        }
    }
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.print(PROMPT);
                if (!scanner.hasNextLine())
                    break;

                String input = scanner.nextLine();
                String tokens[] = input.trim().split("\\s+");
                if (tokens.length == 1 && tokens[0].isEmpty()) {
                    continue;
                }
                String cmd = tokens[0];
                String arg[] = tokens.length > 1 ? Arrays.copyOfRange(tokens, 1, tokens.length) : new String[0];
                switch (cmd) {
                    case "exit" -> {
                        return;
                    }
                    case "echo" ->
                        System.out.println(input.length() > 5 && input.startsWith("echo ") ? input.substring(5) : "");
                    case "type" -> handleType(arg);
                    case "pwd" -> getCurrentDirectory();
                    case "cd" -> changeDirectory(arg);
                    default -> handleExternalCommand(cmd, arg);
                }
            }
        }
    }

    public static void changeDirectory(String[] args) {
        if (args.length == 0) { return; }

        String path = args[0];
        File target = new File(path);
        if (!target.exists() || !target.isDirectory()) {
            System.err.println("cd: " + path + ": No such file or directory");
            return;
        }
        try {
            currentDir = target.getCanonicalFile();
        } catch (IOException e) {
            currentDir = target.getAbsoluteFile();
        }
    }

    private static void handleType(String[] args) {
        if (args.length == 0) {
            return;
        }
        String target = args[0];
        if (BUILTINS.contains(target)) {
            System.out.println(target + " is a shell builtin");
            return;
        }
        File exe = findExecutable(target);
        if (exe != null) {
            System.out.println(target + " is " + exe.getAbsolutePath());
        } else {
            System.out.println(target + " not found");
        }
    }

    private static void handleExternalCommand(String cmd, String[] args) {
        File exe = findExecutable(cmd);
        if (exe == null) {
            System.out.println(cmd + ": command not found");
            return;
        }
        List<String> command = new ArrayList<>();
        command.add(cmd);
        command.addAll(Arrays.asList(args));
        ProcessBuilder pb = new ProcessBuilder(command);
        pb.inheritIO();
        try {
            Process process = pb.start();
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            System.err.println("Error executing command: " + e.getMessage());
        }
    }

    private static File findExecutable(String name) {
        String pathEnv = System.getenv("PATH");
        if (pathEnv == null || pathEnv.isEmpty()) {
            return null;
        }
        String[] paths = pathEnv.split(File.pathSeparator);
        for (String dir : paths) {
            File file = new File(dir, name);
            if (file.exists() && file.canExecute()) {
                return file;
            }
        }
        return null;
    }

    private static void getCurrentDirectory() {
        System.out.println(currentDir.getAbsolutePath());
    }
}
