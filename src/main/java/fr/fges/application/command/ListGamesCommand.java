package fr.fges.application.command;

import fr.fges.domain.model.BoardGame;
import fr.fges.domain.service.GameService;

import java.util.List;

/**
 * ListGamesCommand - Commande pour lister tous les jeux
 */
public class ListGamesCommand implements Command {
    private final GameService gameService;

    public ListGamesCommand(GameService gameService) {
        this.gameService = gameService;
    }

    @Override
    public void execute() {
        List<BoardGame> sortedGames = gameService.getSortedGames();

        if (sortedGames.isEmpty()) {
            System.out.println("No board games in collection.");
            return;
        }

        for (BoardGame game : sortedGames) {
            System.out.println("Game: " + game.title() + " (" + game.minPlayers() + "-"
                    + game.maxPlayers() + " players) - " + game.category());
        }
    }
}
