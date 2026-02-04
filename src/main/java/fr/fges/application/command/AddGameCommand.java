package fr.fges.application.command;

import fr.fges.domain.model.BoardGame;
import fr.fges.domain.service.GameService;
import fr.fges.exceptions.DuplicateGameException;
import fr.fges.presentation.InputHandler;

/**
 * AddGameCommand - Commande pour ajouter un jeu
 */
public class AddGameCommand implements Command {
    private final GameService gameService;
    private final InputHandler inputHandler;

    public AddGameCommand(GameService gameService, InputHandler inputHandler) {
        this.gameService = gameService;
        this.inputHandler = inputHandler;
    }

    @Override
    public void execute() {
        String title = inputHandler.getInput("Title");
        String minPlayersStr = inputHandler.getInput("Minimum Players");
        String maxPlayersStr = inputHandler.getInput("Maximum Players");
        String category = inputHandler.getInput("Category (e.g., fantasy, cooperative, family, strategy)");

        int minPlayers = Integer.parseInt(minPlayersStr);
        int maxPlayers = Integer.parseInt(maxPlayersStr);

        BoardGame game = new BoardGame(title, minPlayers, maxPlayers, category);

        try {
            gameService.addGame(game);
            System.out.println("Board game added successfully.");
        } catch (DuplicateGameException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
