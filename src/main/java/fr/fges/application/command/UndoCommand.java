package fr.fges.application.command;

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
        // Étape 1 : Vérifie s'il y a des actions à annuler
        if (!gameService.hasActionsToUndo()) {
            System.out.println("Nothing to undo.");
            return; // Termine ici car rien à faire
        }

        // Étape 2 : Récupère la dernière action SANS la retirer (peek)
        Action lastAction = gameService.getLastAction();

        // Étape 3 : Annule effectivement l'action
        boolean success = gameService.undo();

        // Étape 4 : Affiche le résultat
        if (success) {
            // Affiche le message d'annulation personnalisé de l'action
            System.out.println("Undone: " + lastAction.getUndoMessage());
        } else {
            // Ne devrait jamais arriver car on a vérifié hasActionsToUndo() avant
            System.out.println("Failed to undo action.");
        }
    }

    @Override
    public String getName() {
        return "Undo last action";
    }
}
