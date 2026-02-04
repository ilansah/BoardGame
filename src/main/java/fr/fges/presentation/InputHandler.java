package fr.fges.presentation;

import java.util.Scanner;

/**
 * InputHandler - Gestion centralisée des entrées utilisateur
 */
public class InputHandler {
    private final Scanner scanner;

    public InputHandler() {
        this.scanner = new Scanner(System.in);
    }

    public String getInput(String prompt) {
        System.out.printf("%s: ", prompt);
        return scanner.nextLine();
    }

    public String getInput() {
        return scanner.nextLine();
    }

    public void close() {
        scanner.close();
    }
}
