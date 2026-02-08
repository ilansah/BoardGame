package fr.fges.domain.model;

import fr.fges.domain.service.GameService;

/**
 * AddAction - Encapsule l'action d'ajout d'un jeu
 * 
 * Cette classe représente une action "ajouter un jeu" qui peut être annulée.
 * Elle stocke le jeu qui a été ajouté pour pouvoir le supprimer lors du undo.
 * 
 * Exemple de flux :
 * 1. User ajoute "Catan" -> GameService crée AddAction(Catan) et l'empile
 * 2. User fait Undo -> AddAction.undo() est appelée
 * 3. undo() supprime "Catan" de la collection
 */
public class AddAction implements Action {
    // Le jeu qui a été ajouté (on le garde en mémoire pour pouvoir le supprimer)
    private final BoardGame game;
    
    // Référence au service pour pouvoir modifier la collection lors du undo
    private final GameService gameService;

    /**
     * Constructeur - Crée une action d'ajout
     * 
     * @param game Le jeu qui a été ajouté
     * @param gameService Le service pour accéder aux méthodes de modification
     */
    public AddAction(BoardGame game, GameService gameService) {
        this.game = game;
        this.gameService = gameService;
    }

    /**
     * Annule l'ajout en SUPPRIMANT le jeu
     * 
     * IMPORTANT : Utilise removeGameDirectly() au lieu de removeGame()
     * Pourquoi ? Pour éviter de créer une nouvelle RemoveAction dans l'historique
     * Sinon on aurait une boucle infinie d'actions !
     */
    @Override
    public void undo() {
        // Supprime le jeu sans créer d'action dans l'historique
        gameService.removeGameDirectly(game.title());
    }

    /**
     * Description de l'action effectuée
     * Ex: "Add \"Catan\""
     */
    @Override
    public String getDescription() {
        return "Add \"" + game.title() + "\"";
    }

    /**
     * Message à afficher lors de l'annulation
     * 
     * On décrit CE QUI A ÉTÉ FAIT pour annuler (on a supprimé le jeu)
     * Ex: "Removed \"Catan\" from collection."
     */
    @Override
    public String getUndoMessage() {
        return "Removed \"" + game.title() + "\" from collection.";
    }
}
