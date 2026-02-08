package fr.fges.domain.model;

import fr.fges.domain.service.GameService;

/**
 * RemoveAction - Encapsule l'action de suppression d'un jeu
 * 
 * Cette classe représente une action "supprimer un jeu" qui peut être annulée.
 * CRUCIAL : Elle stocke le jeu COMPLET avant sa suppression pour pouvoir le restaurer.
 * 
 * Exemple de flux :
 * 1. User supprime "Catan" -> GameService crée RemoveAction(Catan) AVANT de supprimer
 * 2. Le jeu est supprimé de la collection
 * 3. User fait Undo -> RemoveAction.undo() est appelée
 * 4. undo() ré-ajoute "Catan" avec toutes ses infos (min/max players, category)
 */
public class RemoveAction implements Action {
    // Le jeu qui a été supprimé (on garde TOUT l'objet pour pouvoir le restaurer)
    // C'est pour ça qu'on capture le jeu AVANT de le supprimer dans GameService.removeGame()
    private final BoardGame game;
    
    // Référence au service pour pouvoir modifier la collection lors du undo
    private final GameService gameService;

    /**
     * Constructeur - Crée une action de suppression
     * 
     * @param game Le jeu qui a été supprimé (capturé AVANT la suppression !)
     * @param gameService Le service pour accéder aux méthodes de modification
     */
    public RemoveAction(BoardGame game, GameService gameService) {
        this.game = game;
        this.gameService = gameService;
    }

    /**
     * Annule la suppression en RÉ-AJOUTANT le jeu
     * 
     * IMPORTANT : Utilise addGameDirectly() au lieu de addGame()
     * Pourquoi ? Pour éviter de créer une nouvelle AddAction dans l'historique
     * Sinon on aurait une boucle infinie d'actions !
     */
    @Override
    public void undo() {
        // Ré-ajoute le jeu sans créer d'action dans l'historique
        gameService.addGameDirectly(game);
    }

    /**
     * Description de l'action effectuée
     * Ex: "Remove \"7 Wonders\""
     */
    @Override
    public String getDescription() {
        return "Remove \"" + game.title() + "\"";
    }

    /**
     * Message à afficher lors de l'annulation
     * 
     * On décrit CE QUI A ÉTÉ FAIT pour annuler (on a ré-ajouté le jeu)
     * Ex: "Re-added \"7 Wonders\" to collection."
     */
    @Override
    public String getUndoMessage() {
        return "Re-added \"" + game.title() + "\" to collection.";
    }
}
