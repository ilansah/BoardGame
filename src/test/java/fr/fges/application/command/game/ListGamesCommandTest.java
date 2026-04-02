package fr.fges.application.command.game;

import fr.fges.application.command.CommandTestSupport;
import fr.fges.domain.model.BoardGame;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@DisplayName("ListGamesCommand Tests")
class ListGamesCommandTest extends CommandTestSupport {
    private ListGamesCommand command;

    @BeforeEach
    void setUp() {
        setUpCommon();
        command = new ListGamesCommand(gameService);
    }

    @AfterEach
    void tearDown() {
        restoreConsole();
    }

    @Test
    @DisplayName("getName doit retourner le libelle de la commande")
    void getName_shouldReturnCommandLabel() {
        assertEquals("List all games", command.getName());
    }

    @Test
    @DisplayName("execute doit afficher un message si la collection est vide")
    void execute_shouldPrintMessageWhenCollectionIsEmpty() {
        when(gameService.getSortedGames()).thenReturn(List.of());

        command.execute();

        assertEquals("No board games in collection.\n", getConsoleOutput());
    }

    @Test
    @DisplayName("execute doit afficher les jeux tries")
    void execute_shouldPrintSortedGames() {
        when(gameService.getSortedGames()).thenReturn(List.of(
                new BoardGame("Azul", 2, 4, "family"),
                new BoardGame("Catan", 3, 4, "strategy")
        ));

        command.execute();

        assertEquals(
                "Game: Azul (2-4 players) - family\nGame: Catan (3-4 players) - strategy\n",
                getConsoleOutput()
        );
    }
}