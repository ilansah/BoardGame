package fr.fges.application.command;

import fr.fges.domain.model.BoardGame;
import fr.fges.domain.service.GameService;
import fr.fges.presentation.InputHandler;

import java.util.List;

/**
 * FindGamesByPlayerCountCommand - Commande pour trouver tous les jeux compatibles avec un nombre de joueurs
 */
public class FindGamesByPlayerCountCommand implements Command {
    private final GameService gameService;
    private final InputHandler inputHandler;

    public FindGamesByPlayerCountCommand(GameService gameService, InputHandler inputHandler) {
        this.gameService = gameService;
        this.inputHandler = inputHandler;
    }

    @Override
    public void execute() {
        String playerCountStr = inputHandler.getInput("How many players?");
        int playerCount = Integer.parseInt(playerCountStr);

        List<BoardGame> compatibleGames = gameService.findGamesByPlayerCount(playerCount);

        if (compatibleGames.isEmpty()) {
            System.out.println("No game found for " + playerCount + " players.");
        } else {
            System.out.println("\n=== Games for " + playerCount + " players ===");
            for (BoardGame game : compatibleGames) {
                System.out.println("\"" + game.title() + "\" ("
                        + game.minPlayers() + "-" + game.maxPlayers()
                        + " players, " + game.category() + ")");
            }
        }
    }
}
