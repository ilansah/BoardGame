package fr.fges.application.command;

import fr.fges.domain.model.BoardGame;
import fr.fges.domain.service.GameService;
import fr.fges.presentation.InputHandler;

/**
 * RecommendGameCommand - Commande pour recommander un jeu
 */
public class RecommendGameCommand implements Command {
    private final GameService gameService;
    private final InputHandler inputHandler;

    public RecommendGameCommand(GameService gameService, InputHandler inputHandler) {
        this.gameService = gameService;
        this.inputHandler = inputHandler;
    }

    @Override
    public void execute() {
        String playerCountStr = inputHandler.getInput("How many players?");
        int playerCount = Integer.parseInt(playerCountStr);

        BoardGame recommended = gameService.recommendGame(playerCount);

        if (recommended == null) {
            System.out.println("No game found for " + playerCount + " players.");
        } else {
            System.out.println("Recommended game: \"" + recommended.title() + "\" ("
                    + recommended.minPlayers() + "-" + recommended.maxPlayers()
                    + " players, " + recommended.category() + ")");
        }
    }
}
