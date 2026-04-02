package fr.fges.domain.service;

import fr.fges.domain.model.Action;
import fr.fges.domain.model.ActionHistory;
import fr.fges.domain.model.AddAction;
import fr.fges.domain.model.BoardGame;
import fr.fges.domain.model.RemoveAction;

/**
 * Service specialise pour la gestion de l'historique des actions.
 */
public class GameHistoryService {
    private final ActionHistory actionHistory;

    public GameHistoryService() {
        this.actionHistory = new ActionHistory();
    }

    public void recordAddAction(BoardGame game, GameService gameService) {
        actionHistory.addAction(new AddAction(game, gameService));
    }

    public void recordRemoveAction(BoardGame game, GameService gameService) {
        actionHistory.addAction(new RemoveAction(game, gameService));
    }

    public boolean undo() {
        return actionHistory.undoLastAction();
    }

    public boolean hasActionsToUndo() {
        return actionHistory.hasActions();
    }

    public Action getLastAction() {
        return actionHistory.peekLastAction();
    }
}