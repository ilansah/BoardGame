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

@DisplayName("FindGamesByPlayerCountCommand Tests")
class FindGamesByPlayerCountCommandTest extends CommandTestSupport {
    private FindGamesByPlayerCountCommand command;

    @BeforeEach
    void setUp() {
        setUpCommon();
        command = new FindGamesByPlayerCountCommand(gameService, inputHandler);
    }

    @AfterEach
    void tearDown() {
        restoreConsole();
    }

    @Test
    @DisplayName("getName doit retourner le libelle de la commande")
    void getName_shouldReturnCommandLabel() {
        assertEquals("Find games by player count", command.getName());
    }

    @Test
    @DisplayName("execute doit afficher qu'aucun jeu n'est trouve")
    void execute_shouldPrintNoGameMessageWhenNoMatch() {
        when(inputHandler.getInput("How many players?")).thenReturn("10");
        when(gameService.findGamesByPlayerCount(10)).thenReturn(List.of());

        command.execute();

        assertEquals("No game found for 10 players.\n", getConsoleOutput());
    }

    @Test
    @DisplayName("execute doit afficher les jeux compatibles")
    void execute_shouldPrintCompatibleGames() {
        when(inputHandler.getInput("How many players?")).thenReturn("3");
        when(gameService.findGamesByPlayerCount(3)).thenReturn(List.of(
                new BoardGame("Azul", 2, 4, "family"),
                new BoardGame("Catan", 3, 4, "strategy")
        ));

        command.execute();

        assertEquals(
                "\n=== Games for 3 players ===\n\"Azul\" (2-4 players, family)\n\"Catan\" (3-4 players, strategy)\n",
                getConsoleOutput()
        );
    }
}