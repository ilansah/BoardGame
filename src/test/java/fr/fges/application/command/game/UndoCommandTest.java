package fr.fges.application.command.game;

import fr.fges.application.command.CommandTestSupport;
import fr.fges.domain.model.Action;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("UndoCommand Tests")
class UndoCommandTest extends CommandTestSupport {
    private UndoCommand command;

    @BeforeEach
    void setUp() {
        setUpCommon();
        command = new UndoCommand(gameService);
    }

    @AfterEach
    void tearDown() {
        restoreConsole();
    }

    @Test
    @DisplayName("getName doit retourner le libelle de la commande")
    void getName_shouldReturnCommandLabel() {
        assertEquals("Undo last action", command.getName());
    }

    @Test
    @DisplayName("execute doit afficher qu'il n'y a rien a annuler")
    void execute_shouldPrintNothingToUndoWhenEmpty() {
        when(gameService.hasActionsToUndo()).thenReturn(false);

        command.execute();

        assertEquals("Nothing to undo.\n", getConsoleOutput());
    }

    @Test
    @DisplayName("execute doit afficher le message d'annulation")
    void execute_shouldPrintUndoMessage() {
        Action lastAction = mock(Action.class);
        when(gameService.hasActionsToUndo()).thenReturn(true);
        when(gameService.getLastAction()).thenReturn(lastAction);
        when(lastAction.getUndoMessage()).thenReturn("Removed \"Catan\" from collection.");
        when(gameService.undo()).thenReturn(true);

        command.execute();

        assertEquals("Undone: Removed \"Catan\" from collection.\n", getConsoleOutput());
    }
}