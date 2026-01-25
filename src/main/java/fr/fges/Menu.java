package fr.fges;

import java.util.Scanner;

public class Menu {
    private final GameCollection gameCollection;

    public Menu(GameCollection gameCollection) {
        this.gameCollection = gameCollection;
    }

    public static String getUserInput(String prompt) {
        // Scanner is a class in java that helps to read input from various sources like keyboard input, files, etc.
        Scanner scanner = new Scanner(System.in);
        // No new line for this one
        System.out.printf("%s: ", prompt);
        // Read input for the keyboard
        return scanner.nextLine();
    }

    public static void displayMainMenu() {
        String menuText = """
                === Board Game Collection ===
                1. Add Board Game
                2. Remove Board Game
                3. List All Board Games
                4. Exit
                Please select an option (1-4):
                """;

        System.out.println(menuText);
    }

    public void addGame() {
        String title = getUserInput("Title");
        String minPlayersStr = getUserInput("Minimum Players");
        String maxPlayersStr = getUserInput("Maximum Players");
        String category = getUserInput("Category (e.g., fantasy, cooperative, family, strategy)");

        int minPlayers = Integer.parseInt(minPlayersStr);
        int maxPlayers = Integer.parseInt(maxPlayersStr);

        BoardGame game = new BoardGame(title, minPlayers, maxPlayers, category);

        gameCollection.addGame(game);
        System.out.println("Board game added successfully.");
    }

    public void removeGame() {
        String title = getUserInput("Title of game to remove");
        var games = gameCollection.getGames();
        for (BoardGame game : games) {
            if (game.title().equals(title)) {
                gameCollection.removeGame(game);
                System.out.println("Board game removed successfully.");
                return;
            }
        }
        System.out.println("No board game found with that title.");
    }

    public void listAllGames() {
        gameCollection.viewAllGames();
    }

    public void exit() throws MenuExitException {
        System.out.println("Exiting the application. Goodbye!");
        throw new MenuExitException();
    }

    public void handleMenu() throws MenuExitException {
        displayMainMenu();

        Scanner scanner = new Scanner(System.in);
        String choice = scanner.nextLine();

        switch (choice) {
            case "1" -> addGame();
            case "2" -> removeGame();
            case "3" -> listAllGames();
            case "4" -> exit();
            default -> System.out.println("Invalid choice. Please select a valid option.");
        }
    }
// Exception pour gérer la sortie du menu sans System.exit
class MenuExitException extends RuntimeException {
    public MenuExitException() {
        super("Menu exited by user");
    }
}
}
