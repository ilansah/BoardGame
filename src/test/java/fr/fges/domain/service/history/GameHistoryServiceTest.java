package fr.fges.domain.service.history;

import fr.fges.domain.model.Action;
import fr.fges.domain.model.BoardGame;
import fr.fges.domain.service.GameHistoryService;
import fr.fges.domain.service.GameMutationPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@DisplayName("GameHistoryService Tests")
class GameHistoryServiceTest {

    @Test
    @DisplayName("hasActionsToUndo doit retourner false quand l'historique est vide")
    void hasActionsToUndo_shouldReturnFalseWhenHistoryIsEmpty() {
        GameHistoryService gameHistoryService = new GameHistoryService();

        assertFalse(gameHistoryService.hasActionsToUndo());
        assertNull(gameHistoryService.getLastAction());
        assertFalse(gameHistoryService.undo());
    }

    @Test
    @DisplayName("recordAddAction doit enregistrer une action annulable")
    void recordAddAction_shouldRecordUndoableAction() {
        GameHistoryService gameHistoryService = new GameHistoryService();
        GameMutationPort mutationPort = mock(GameMutationPort.class);
        BoardGame game = new BoardGame("Catan", 3, 4, "strategy");

        gameHistoryService.recordAddAction(game, mutationPort);

        assertTrue(gameHistoryService.hasActionsToUndo());
        Action lastAction = gameHistoryService.getLastAction();
        assertNotNull(lastAction);
        assertEquals("Add \"Catan\"", lastAction.getDescription());

        assertTrue(gameHistoryService.undo());
        verify(mutationPort).removeGameDirectly("Catan");
    }

    @Test
    @DisplayName("recordRemoveAction doit enregistrer une action annulable")
    void recordRemoveAction_shouldRecordUndoableAction() {
        GameHistoryService gameHistoryService = new GameHistoryService();
        GameMutationPort mutationPort = mock(GameMutationPort.class);
        BoardGame game = new BoardGame("Azul", 2, 4, "family");

        gameHistoryService.recordRemoveAction(game, mutationPort);

        assertTrue(gameHistoryService.hasActionsToUndo());
        Action lastAction = gameHistoryService.getLastAction();
        assertNotNull(lastAction);
        assertEquals("Remove \"Azul\"", lastAction.getDescription());

        assertTrue(gameHistoryService.undo());
        verify(mutationPort).addGameDirectly(game);
    }
}