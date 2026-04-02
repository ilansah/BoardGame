package fr.fges.application.command.tournament;

import fr.fges.domain.service.GameService;
import fr.fges.domain.service.TournamentService;
import fr.fges.presentation.InputHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

@DisplayName("TournamentMenuCommand Tests")
class TournamentMenuCommandTest {
    private TournamentMenuCommand command;
    private GameService gameService;
    private TournamentService tournamentService;
    private InputHandler inputHandler;

    @BeforeEach
    void setUp() {
        gameService = mock(GameService.class);
        tournamentService = mock(TournamentService.class);
        inputHandler = mock(InputHandler.class);
        command = new TournamentMenuCommand(gameService, tournamentService, inputHandler);
    }

    @Test
    @DisplayName("TournamentMenuCommand doit se creer correctement")
    void shouldCreateInstanceSuccessfully() {
        assertNotNull(command);
    }

    @Test
    @DisplayName("getName doit retourner Tournament")
    void getName_shouldReturnTournament() {
        assertEquals("Tournament", command.getName());
    }
}
