package fr.fges.domain.model;

import fr.fges.domain.service.GameService;

/**
 * AddAction - Encapsule l'action d'ajout d'un jeu
 */
public class AddAction implements Action {
    private final BoardGame game;
    
    private final GameService gameService;

    public AddAction(BoardGame game, GameService gameService) {
        this.game = game;
        this.gameService = gameService;
    }


    @Override
    public void undo() {
        // Supprime le jeu sans créer d'action dans l'historique
        gameService.removeGameDirectly(game.title());
    }

    @Override
    public String getDescription() {
        return "Add \"" + game.title() + "\"";
    }

    @Override
    public String getUndoMessage() {
        return "Removed \"" + game.title() + "\" from collection.";
    }
}
