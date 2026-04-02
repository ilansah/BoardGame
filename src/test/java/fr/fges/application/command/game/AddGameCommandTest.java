package fr.fges.application.command.game;

import fr.fges.application.command.CommandTestSupport;
import fr.fges.domain.model.BoardGame;
import fr.fges.exceptions.DuplicateGameException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("AddGameCommand Tests")
class AddGameCommandTest extends CommandTestSupport {
    private AddGameCommand command;

    @BeforeEach
    void setUp() {
        setUpCommon();
        command = new AddGameCommand(gameService, inputHandler);
    }

    @AfterEach
    void tearDown() {
        restoreConsole();
    }

    @Test
    @DisplayName("getName doit retourner le libelle de la commande")
    void getName_shouldReturnCommandLabel() {
        assertEquals("Add a game", command.getName());
    }

    @Test
    @DisplayName("execute doit ajouter un jeu et afficher un message de succes")
    void execute_shouldAddGameAndPrintSuccessMessage() {
        when(inputHandler.getInput("Title")).thenReturn("Pandemic");
        when(inputHandler.getInput("Minimum Players")).thenReturn("2");
        when(inputHandler.getInput("Maximum Players")).thenReturn("4");
        when(inputHandler.getInput("Category (e.g., fantasy, cooperative, family, strategy)")).thenReturn("cooperative");

        command.execute();

        verify(gameService).addGame(new BoardGame("Pandemic", 2, 4, "cooperative"));
        assertEquals("Board game added successfully.\n", getConsoleOutput());
    }

    @Test
    @DisplayName("execute doit afficher une erreur en cas de doublon")
    void execute_shouldPrintErrorWhenDuplicate() {
        when(inputHandler.getInput("Title")).thenReturn("Catan");
        when(inputHandler.getInput("Minimum Players")).thenReturn("3");
        when(inputHandler.getInput("Maximum Players")).thenReturn("4");
        when(inputHandler.getInput("Category (e.g., fantasy, cooperative, family, strategy)")).thenReturn("strategy");
        doThrow(new DuplicateGameException("Catan")).when(gameService).addGame(new BoardGame("Catan", 3, 4, "strategy"));

        command.execute();

        assertEquals("Error: Un jeu avec le titre \"Catan\" existe déjà dans la collection\n", getConsoleOutput());
    }
}