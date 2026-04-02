package fr.fges.domain.service.management;

import fr.fges.domain.model.BoardGame;
import fr.fges.domain.service.GameManagementService;
import fr.fges.domain.service.GameServiceTestSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("GameManagementService Query Tests")
class GameManagementServiceQueryTest extends GameServiceTestSupport {
    private GameManagementService gameManagementService;

    @BeforeEach
    void setUp() {
        initRepositoryWithDefaultGames();
        gameManagementService = new GameManagementService(mockRepository);
    }

    @Test
    @DisplayName("getAllGames doit retourner une copie défensive de la liste")
    void getAllGames_shouldReturnDefensiveCopy() {
        List<BoardGame> games = gameManagementService.getAllGames();
        games.clear();

        assertEquals(3, gameManagementService.getAllGames().size());
    }

    @Test
    @DisplayName("getSortedGames doit retourner les jeux triés par titre")
    void getSortedGames_shouldReturnGamesSortedByTitle() {
        List<BoardGame> sortedGames = gameManagementService.getSortedGames();

        assertEquals("7 Wonders", sortedGames.get(0).title());
        assertEquals("Azul", sortedGames.get(1).title());
        assertEquals("Catan", sortedGames.get(2).title());
    }

    @Test
    @DisplayName("findByTitle doit retourner le jeu correspondant")
    void findByTitle_shouldReturnMatchingGame() {
        Optional<BoardGame> result = gameManagementService.findByTitle("Azul");

        assertTrue(result.isPresent());
        assertEquals("Azul", result.get().title());
        assertEquals(2, result.get().minPlayers());
        assertEquals(4, result.get().maxPlayers());
        assertEquals("family", result.get().category());
    }

    @Test
    @DisplayName("findByTitle doit retourner Optional.empty si le jeu n'existe pas")
    void findByTitle_shouldReturnEmptyIfGameDoesNotExist() {
        Optional<BoardGame> result = gameManagementService.findByTitle("Jeu Inexistant");

        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("isEmpty doit retourner false quand il y a des jeux")
    void isEmpty_shouldReturnFalseWhenGamesExist() {
        assertFalse(gameManagementService.isEmpty());
    }

    @Test
    @DisplayName("isEmpty doit retourner true quand il n'y a pas de jeux")
    void isEmpty_shouldReturnTrueWhenNoGames() {
        initRepositoryWithGames(new ArrayList<>());
        GameManagementService emptyService = new GameManagementService(mockRepository);

        assertTrue(emptyService.isEmpty());
    }

    @Test
    @DisplayName("existsByTitle doit retourner true pour un titre existant")
    void existsByTitle_withExistingTitle_shouldReturnTrue() {
        assertTrue(gameManagementService.existsByTitle("Catan"));
        assertTrue(gameManagementService.existsByTitle("catan"));
        assertTrue(gameManagementService.existsByTitle("CATAN"));
    }

    @Test
    @DisplayName("existsByTitle doit retourner false pour un titre inexistant")
    void existsByTitle_withNonExistingTitle_shouldReturnFalse() {
        assertFalse(gameManagementService.existsByTitle("Monopoly"));
        assertFalse(gameManagementService.existsByTitle(""));
    }
}