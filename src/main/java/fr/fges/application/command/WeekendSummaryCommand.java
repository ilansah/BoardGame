package fr.fges.application.command;

import fr.fges.domain.model.BoardGame;
import fr.fges.domain.service.GameService;

import java.util.List;

/**
 * WeekendSummaryCommand - Commande pour afficher le résumé du weekend
 */
public class WeekendSummaryCommand implements Command {
    private final GameService gameService;

    public WeekendSummaryCommand(GameService gameService) {
        this.gameService = gameService;
    }

    @Override
    public void execute() {
        List<BoardGame> randomGames = gameService.getRandomGames(3);

        System.out.println("\n=== Summary (" + randomGames.size() + " random games) ===");

        if (randomGames.isEmpty()) {
            System.out.println("No game in your collection");
        } else {
            for (BoardGame game : randomGames) {
                System.out.println("- " + game.title() + " (" + game.minPlayers() + "-"
                        + game.maxPlayers() + " players, " + game.category() + ")");
            }
        }

        System.out.println();
    }
}
