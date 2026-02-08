package fr.fges.application.command;

import fr.fges.domain.model.Action;
import fr.fges.domain.service.GameService;

/**
 * UndoCommand - Commande pour annuler la dernière action
 * 
 * Cette commande implémente l'interface Command et permet à l'utilisateur
 * d'annuler sa dernière action (Add ou Remove).
 * 
 * Flux d'exécution :
 * 1. Vérifie s'il y a des actions à annuler
 * 2. Si oui, récupère la dernière action (pour le message)
 * 3. Exécute l'annulation via GameService
 * 4. Affiche le message d'annulation
 * 
 * Exemple :
 * User ajoute "Catan" puis fait Undo
 * -> Affiche : "Undone: Removed \"Catan\" from collection."
 */
public class UndoCommand implements Command {
    // Référence au service pour accéder aux méthodes de gestion du undo
    private final GameService gameService;

    /**
     * Constructeur - Initialise la commande avec le service
     * 
     * @param gameService Le service qui gère la collection et l'historique
     */
    public UndoCommand(GameService gameService) {
        this.gameService = gameService;
    }

    /**
     * Exécute la commande d'annulation
     * 
     * Étapes détaillées :
     * 1. Vérifie si ActionHistory contient des actions
     * 2. Si non -> affiche "Nothing to undo." et termine
     * 3. Si oui -> récupère la dernière action avec peek() (sans la retirer)
     * 4. Appelle undo() qui va pop() l'action et l'annuler
     * 5. Affiche le message d'annulation de l'action
     */
    @Override
    public void execute() {
        // Étape 1 : Vérifie s'il y a des actions à annuler
        // GameService.hasActionsToUndo() -> ActionHistory.hasActions() -> !stack.isEmpty()
        if (!gameService.hasActionsToUndo()) {
            System.out.println("Nothing to undo.");
            return; // Termine ici car rien à faire
        }

        // Étape 2 : Récupère la dernière action SANS la retirer (peek)
        // On en a besoin pour afficher le message après l'annulation
        Action lastAction = gameService.getLastAction();

        // Étape 3 : Annule effectivement l'action
        // GameService.undo() -> ActionHistory.undoLastAction()
        // -> pop() l'action et appelle action.undo()
        boolean success = gameService.undo();

        // Étape 4 : Affiche le résultat
        if (success) {
            // Affiche le message d'annulation personnalisé de l'action
            // Ex: "Undone: Removed \"Catan\" from collection."
            System.out.println("Undone: " + lastAction.getUndoMessage());
        } else {
            // Ne devrait jamais arriver car on a vérifié hasActionsToUndo() avant
            System.out.println("Failed to undo action.");
        }
    }
}
