package fr.fges.application.command.game;

import fr.fges.application.command.Command;
import fr.fges.domain.model.Action;
import fr.fges.domain.service.GameService;

/**
 * UndoCommand - Commande pour annuler la dernière action
 */
public class UndoCommand implements Command {
    private final GameService gameService;

    public UndoCommand(GameService gameService) {
        this.gameService = gameService;
    }

    @Override
    public void execute() {
        if (!gameService.hasActionsToUndo()) {
            System.out.println("Nothing to undo.");
            return;
        }

        Action lastAction = gameService.getLastAction();
        boolean success = gameService.undo();

        if (success) {
            System.out.println("Undone: " + lastAction.getUndoMessage());
        } else {
            System.out.println("Failed to undo action.");
        }
    }

    @Override
    public String getName() {
        return "Undo last action";
    }
}