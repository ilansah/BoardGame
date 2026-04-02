package fr.fges.domain.model;

import fr.fges.domain.service.GameMutationPort;

/**
 * AddAction - Encapsule l'action d'ajout d'un jeu
 */
public class AddAction implements Action {
    private final BoardGame game;
    
    private final GameMutationPort gameMutationPort;

    public AddAction(BoardGame game, GameMutationPort gameMutationPort) {
        this.game = game;
        this.gameMutationPort = gameMutationPort;
    }


    @Override
    public void undo() {
        // Supprime le jeu sans créer d'action dans l'historique
        gameMutationPort.removeGameDirectly(game.title());
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
