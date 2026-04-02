package fr.fges.application.command.game;

import fr.fges.application.command.CommandTestSupport;
import fr.fges.domain.model.BoardGame;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@DisplayName("RecommendGameCommand Tests")
class RecommendGameCommandTest extends CommandTestSupport {
    private RecommendGameCommand command;

    @BeforeEach
    void setUp() {
        setUpCommon();
        command = new RecommendGameCommand(gameService, inputHandler);
    }

    @AfterEach
    void tearDown() {
        restoreConsole();
    }

    @Test
    @DisplayName("getName doit retourner le libelle de la commande")
    void getName_shouldReturnCommandLabel() {
        assertEquals("Recommend a game", command.getName());
    }

    @Test
    @DisplayName("execute doit afficher qu'aucun jeu n'est trouve")
    void execute_shouldPrintNoGameMessageWhenNoMatch() {
        when(inputHandler.getInput("How many players?")).thenReturn("5");
        when(gameService.recommendGame(5)).thenReturn(null);

        command.execute();

        assertEquals("No game found for 5 players.\n", getConsoleOutput());
    }

    @Test
    @DisplayName("execute doit afficher la recommandation")
    void execute_shouldPrintRecommendation() {
        when(inputHandler.getInput("How many players?")).thenReturn("3");
        when(gameService.recommendGame(3)).thenReturn(new BoardGame("Catan", 3, 4, "strategy"));

        command.execute();

        assertEquals(
                "Recommended game: \"Catan\" (3-4 players, strategy)\n",
                getConsoleOutput()
        );
    }
}