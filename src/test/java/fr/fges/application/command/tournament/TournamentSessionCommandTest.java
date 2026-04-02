package fr.fges.application.command.tournament;

import fr.fges.domain.service.TournamentService;
import fr.fges.presentation.InputHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

@DisplayName("TournamentSessionCommand Tests")
class TournamentSessionCommandTest {
    private TournamentService tournamentService;
    private InputHandler inputHandler;

    @BeforeEach
    void setUp() {
        tournamentService = mock(TournamentService.class);
        inputHandler = mock(InputHandler.class);
    }

    @Test
    @DisplayName("TournamentSessionCommand doit se creer correctement")
    void shouldCreateInstanceSuccessfully() {
        TournamentSessionCommand session = new TournamentSessionCommand(tournamentService, inputHandler);
        assertNotNull(session);
    }
}
