package fr.fges.domain.service.facade;

import fr.fges.domain.model.BoardGame;
import fr.fges.domain.port.GameRepository;
import fr.fges.domain.service.GameService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("GameService Facade Tests")
class GameServiceFacadeTest {

    @Test
    @DisplayName("Le constructeur doit charger les jeux depuis le repository")
    void constructor_shouldLoadGamesFromRepository() {
        GameRepository mockRepository = mock(GameRepository.class);
        when(mockRepository.findAll()).thenReturn(List.of(
                new BoardGame("Catan", 3, 4, "strategy"),
                new BoardGame("Azul", 2, 4, "family")
        ));

        GameService gameService = new GameService(mockRepository);

        assertEquals(2, gameService.getAllGames().size());
    }

    @Test
    @DisplayName("La façade doit exposer les opérations métier principales")
    void facade_shouldExposeMainGameOperations() {
        GameRepository mockRepository = mock(GameRepository.class);
        List<BoardGame> games = new ArrayList<>(List.of(
                new BoardGame("Catan", 3, 4, "strategy"),
                new BoardGame("Azul", 2, 4, "family")
        ));

        when(mockRepository.findAll()).thenAnswer(invocation -> new ArrayList<>(games));
        doAnswer(invocation -> {
            List<BoardGame> savedGames = invocation.getArgument(0);
            games.clear();
            games.addAll(savedGames);
            return null;
        }).when(mockRepository).save(anyList());

        GameService gameService = new GameService(mockRepository);

        gameService.addGame(new BoardGame("Pandemic", 2, 4, "cooperative"));
        assertTrue(gameService.existsByTitle("Pandemic"));

        gameService.removeGame("Catan");
        assertFalse(gameService.existsByTitle("Catan"));
    }
}