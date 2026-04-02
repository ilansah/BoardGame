package fr.fges.domain.model;

import fr.fges.domain.service.GameMutationPort;

/**
 * RemoveAction - Encapsule l'action de suppression d'un jeu
 */
public class RemoveAction implements Action {

    private final BoardGame game;
    
    private final GameMutationPort gameMutationPort;

    public RemoveAction(BoardGame game, GameMutationPort gameMutationPort) {
        this.game = game;
        this.gameMutationPort = gameMutationPort;
    }

    @Override
    public void undo() {
        // Re ajoute le jeu sans créer d'action dans l'historique
        gameMutationPort.addGameDirectly(game);
    }


    @Override
    public String getDescription() {
        return "Remove \"" + game.title() + "\"";
    }

    /**
     * Message à afficher lors de l'annulation
     * On décrit CE QUI A ÉTÉ FAIT pour annuler (on a ré-ajouté le jeu)
     */
    @Override
    public String getUndoMessage() {
        return "Re-added \"" + game.title() + "\" to collection.";
    }
}
