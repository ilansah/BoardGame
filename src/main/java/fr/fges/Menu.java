package fr.fges;

import fr.fges.service.DuplicateGameException;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class Menu {
    private final GameCollection gameCollection;
    private final Scanner scanner;

    public Menu(GameCollection gameCollection) {
        this.gameCollection = gameCollection;
        this.scanner = new Scanner(System.in);
    }

    public String getUserInput(String prompt) {
        // Scanner is a class in java that helps to read input from various sources like
        // keyboard input, files, etc.
        // No new line for this one
        System.out.printf("%s: ", prompt);
        // Read input for the keyboard
        return scanner.nextLine();
    }

    // menu dynamique : 5 options en semaine, 6 options le weekend
    public void displayMainMenu() {
        // on check si c'est le weekend pour adapter le menu
        if (isWeekend()) {
            // menu du weekend avec l'option 5 pour le summary
            String menuText = """
                    === Board Game Collection ===
                    1. Add Board Game
                    2. Remove Board Game
                    3. List All Board Games
                    4. Recommend Game
                    5. Weekend Summary
                    6. Exit
                    Please select an option (1-6):
                    """;
            System.out.println(menuText);
        } else {
            // menu normal de la semaine
            String menuText = """
                    === Board Game Collection ===
                    1. Add Board Game
                    2. Remove Board Game
                    3. List All Board Game
                    4. Recommend Game
                    5. Exit
                    Please select an option (1-5):
                    """;
            System.out.println(menuText);
        }
    }

    public void addGame() {
        String title = getUserInput("Title");
        String minPlayersStr = getUserInput("Minimum Players");
        String maxPlayersStr = getUserInput("Maximum Players");
        String category = getUserInput("Category (e.g., fantasy, cooperative, family, strategy)");

        int minPlayers = Integer.parseInt(minPlayersStr);
        int maxPlayers = Integer.parseInt(maxPlayersStr);

        BoardGame game = new BoardGame(title, minPlayers, maxPlayers, category);

        try {
            gameCollection.addGame(game);
            System.out.println("Board game added successfully.");
        } catch (DuplicateGameException e) {
            System.out.println("Error: " + e.getMessage());
        }
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

    public void recommendGame() {
        String playerCountStr = getUserInput("How many players?");
        int playerCount = Integer.parseInt(playerCountStr);

        BoardGame recommended = gameCollection.recommendGame(playerCount);

        if (recommended == null) {
            System.out.println("No game found for " + playerCount + " players.");
        } else {
            System.out.println("Recommended game: \"" + recommended.title() + "\" ("
                    + recommended.minPlayers() + "-" + recommended.maxPlayers()
                    + " players, " + recommended.category() + ")");
        }
    }

    public void exit() throws MenuExitException {
        System.out.println("Exiting the application. Goodbye!");
        throw new MenuExitException();
    }

    public void handleMenu() throws MenuExitException {
        //on affiche le menu dynamique (5 ou 6 options selon le jour)
        displayMainMenu();

        String choice = scanner.nextLine();

        //le switch change si c le weekend ou pas
        if (isWeekend()) {
            //logique du weekend option 5 = summary et option 6 = exit
            switch (choice) {
                case "1" -> addGame();
                case "2" -> removeGame();
                case "3" -> listAllGames();
                case "4" -> recommendGame();
                case "5" -> displayWeekendSummary();
                case "6" -> exit(); // EXIT en option 6
                default -> System.out.println("Invalide Choice");
            }
        } else {
            //logique de la semaine option 5 = exit
            switch (choice) {
                case "1" -> addGame();
                case "2" -> removeGame();
                case "3" -> listAllGames();
                case "4" -> recommendGame();
                case "5" -> exit(); // EXIT en option 5
                default -> System.out.println("Invalide Choice");
            }
        }
    }

    //check si on est le weekend
    //utilise l'API java.time
    private boolean isWeekend() {
        LocalDate today = LocalDate.now();
        DayOfWeek dayOfWeek = today.getDayOfWeek();
        //samedi = 6, dimanche = 7 dans l'enum DayOfWeek
        return dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY;
    }

    //affiche 3 jeux random pour le wk (ou moins si y'a pas assez)
    //cette fonction est appelé quand user choisit l'option 5 le wk
    private void displayWeekendSummary() {
        List<BoardGame> randomGames = gameCollection.getRandomGames(3);

        //on affiche le nb de jeux trouvés (3 max ou moins si pas assez)
        System.out.println("\n=== Summary (" + randomGames.size() + " random games) ===");

        if (randomGames.isEmpty()) {
            System.out.println("No game in your collection");
        } else {
            //format demandé : - Nom (X-Y players, catégorie)
            for (BoardGame game : randomGames) {
                System.out.println("- " + game.title() + " (" + game.minPlayers() + "-" 
                    + game.maxPlayers() + " players, " + game.category() + ")");
            }
        }

        System.out.println();
    }
}

// Exception pour gérer la sortie du menu sans System.exit
class MenuExitException extends RuntimeException {
    public MenuExitException() {
        super("Menu exited by user");
    }
}
