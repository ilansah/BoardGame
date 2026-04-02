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

@DisplayName("WeekendSummaryCommand Tests")
class WeekendSummaryCommandTest extends CommandTestSupport {
    private WeekendSummaryCommand command;

    @BeforeEach
    void setUp() {
        setUpCommon();
        command = new WeekendSummaryCommand(gameService);
    }

    @AfterEach
    void tearDown() {
        restoreConsole();
    }

    @Test
    @DisplayName("getName doit retourner le libelle de la commande")
    void getName_shouldReturnCommandLabel() {
        assertEquals("Weekend Summary", command.getName());
    }

    @Test
    @DisplayName("execute doit afficher un message quand la collection est vide")
    void execute_shouldPrintMessageWhenCollectionIsEmpty() {
        when(gameService.getRandomGames(3)).thenReturn(List.of());

        command.execute();

        assertEquals("\n=== Summary (0 random games) ===\nNo game in your collection\n\n", getConsoleOutput());
    }

    @Test
    @DisplayName("execute doit afficher les jeux aleatoires")
    void execute_shouldPrintRandomGames() {
        when(gameService.getRandomGames(3)).thenReturn(List.of(
                new BoardGame("Azul", 2, 4, "family"),
                new BoardGame("Catan", 3, 4, "strategy")
        ));

        command.execute();

        assertEquals(
                "\n=== Summary (2 random games) ===\n- Azul (2-4 players, family)\n- Catan (3-4 players, strategy)\n\n",
                getConsoleOutput()
        );
    }
}