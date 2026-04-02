package fr.fges.application.command.game;

import fr.fges.application.command.CommandTestSupport;
import fr.fges.domain.model.BoardGame;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("RemoveGameCommand Tests")
class RemoveGameCommandTest extends CommandTestSupport {
    private RemoveGameCommand command;

    @BeforeEach
    void setUp() {
        setUpCommon();
        command = new RemoveGameCommand(gameService, inputHandler);
    }

    @AfterEach
    void tearDown() {
        restoreConsole();
    }

    @Test
    @DisplayName("getName doit retourner le libelle de la commande")
    void getName_shouldReturnCommandLabel() {
        assertEquals("Remove a game", command.getName());
    }

    @Test
    @DisplayName("execute doit supprimer un jeu existant")
    void execute_shouldRemoveExistingGame() {
        when(inputHandler.getInput("Title of game to remove")).thenReturn("Catan");
        when(gameService.findByTitle("Catan")).thenReturn(Optional.of(new BoardGame("Catan", 3, 4, "strategy")));

        command.execute();

        verify(gameService).removeGame("Catan");
        assertEquals("Board game removed successfully.\n", getConsoleOutput());
    }

    @Test
    @DisplayName("execute doit afficher une erreur si le jeu n'existe pas")
    void execute_shouldPrintErrorWhenGameDoesNotExist() {
        when(inputHandler.getInput("Title of game to remove")).thenReturn("Unknown");
        when(gameService.findByTitle("Unknown")).thenReturn(Optional.empty());

        command.execute();

        assertEquals("No board game found with that title.\n", getConsoleOutput());
    }
}