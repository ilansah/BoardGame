package fr.fges;

import java.util.Scanner;

public class Menu {

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

    public static void addGame() {
        String title = getUserInput("Title");
        String minPlayersStr = getUserInput("Minimum Players");
        String maxPlayersStr = getUserInput("Maximum Players");
        String category = getUserInput("Category (e.g., fantasy, cooperative, family, strategy)");

        int minPlayers = Integer.parseInt(minPlayersStr);
        int maxPlayers = Integer.parseInt(maxPlayersStr);

        BoardGame game = new BoardGame(title, minPlayers, maxPlayers, category);

        GameCollection.addGame(game);
        System.out.println("Board game added successfully.");
    }

    public static void removeGame() {
        String title = getUserInput("Title of game to remove");

        // get games from the collection, find the one that matches the title given by the user and remove
        var games = GameCollection.getGames();

        for (BoardGame game : games) {
            if (game.title().equals(title)) {
                GameCollection.removeGame(game);
                System.out.println("Board game removed successfully.");
                return;
            }
        }
        System.out.println("No board game found with that title.");
    }

    public static void listAllGames() {
        GameCollection.viewAllGames();
    }

    public static void exit() {
        System.out.println("Exiting the application. Goodbye!");
        System.exit(0);
    }

    public static void handleMenu() {
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
}
