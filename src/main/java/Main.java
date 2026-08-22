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
                String message = input.substring(5);
                System.out.println(message);
            } else if (input.startsWith("type ")) {
                String message = input.substring(5);
                if (message.equals("echo") || message.equals("type") || message.equals("exit")) {
                    System.out.println(message + " is a shell builtin");
                }else{
                    System.out.println(message + ": not found");
                }
            } else {
                System.out.println(input + ": command not found");
            }
        }
    }
}
