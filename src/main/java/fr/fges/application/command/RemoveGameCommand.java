package fr.fges.application.command;

import fr.fges.domain.model.BoardGame;
import fr.fges.domain.service.GameService;
import fr.fges.presentation.InputHandler;

import java.util.Optional;

/**
 * RemoveGameCommand - Commande pour supprimer un jeu
 */
public class RemoveGameCommand implements Command {
    private final GameService gameService;
    private final InputHandler inputHandler;

    public RemoveGameCommand(GameService gameService, InputHandler inputHandler) {
        this.gameService = gameService;
        this.inputHandler = inputHandler;
    }

    @Override
    public void execute() {
        String title = inputHandler.getInput("Title of game to remove");

        Optional<BoardGame> game = gameService.findByTitle(title);

        if (game.isPresent()) {
            gameService.removeGame(title);
            System.out.println("Board game removed successfully.");
        } else {
            // Sinon on affiche un message d'erreur
            System.out.println("No board game found with that title.");
        }
    }
}
