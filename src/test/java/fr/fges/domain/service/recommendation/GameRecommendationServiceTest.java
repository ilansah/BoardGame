package fr.fges.domain.service.recommendation;

import fr.fges.domain.model.BoardGame;
import fr.fges.domain.service.GameManagementService;
import fr.fges.domain.service.GameRecommendationService;
import fr.fges.domain.service.GameServiceTestSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("GameRecommendationService Tests")
class GameRecommendationServiceTest extends GameServiceTestSupport {
    private GameRecommendationService recommendationService;

    @BeforeEach
    void setUp() {
        initRepositoryWithDefaultGames();
        GameManagementService gameManagementService = new GameManagementService(mockRepository);
        recommendationService = new GameRecommendationService(gameManagementService);
    }

    @Test
    @DisplayName("recommendGame doit retourner null si aucun jeu ne correspond")
    void recommendGame_shouldReturnNullWhenNoGamesMatch() {
        assertNull(recommendationService.recommendGame(10));
    }

    @Test
    @DisplayName("recommendGame doit retourner un jeu compatible")
    void recommendGame_shouldReturnCompatibleGame() {
        BoardGame recommended = recommendationService.recommendGame(3);

        assertNotNull(recommended);
        assertTrue(recommended.minPlayers() <= 3);
        assertTrue(recommended.maxPlayers() >= 3);
    }

    @Test
    @DisplayName("findGamesByPlayerCount doit retourner tous les jeux compatibles triés alphabétiquement")
    void findGamesByPlayerCount_shouldReturnCompatibleGamesSortedAlphabetically() {
        List<BoardGame> result = recommendationService.findGamesByPlayerCount(3);

        assertEquals(3, result.size());
        assertEquals("7 Wonders", result.get(0).title());
        assertEquals("Azul", result.get(1).title());
        assertEquals("Catan", result.get(2).title());
    }

    @Test
    @DisplayName("findGamesByPlayerCount doit retourner une liste vide si aucun jeu ne correspond")
    void findGamesByPlayerCount_shouldReturnEmptyListWhenNoGamesMatch() {
        List<BoardGame> result = recommendationService.findGamesByPlayerCount(10);

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("findGamesByPlayerCount doit retourner tous les jeux pour un nombre de joueurs dans toutes les plages")
    void findGamesByPlayerCount_shouldReturnAllGamesWhenPlayerCountMatchesAll() {
        List<BoardGame> result = recommendationService.findGamesByPlayerCount(4);

        assertEquals(3, result.size());
        assertEquals("7 Wonders", result.get(0).title());
        assertEquals("Azul", result.get(1).title());
        assertEquals("Catan", result.get(2).title());
    }

    @Test
    @DisplayName("findGamesByPlayerCount doit retourner les jeux pour le nombre minimum de joueurs")
    void findGamesByPlayerCount_shouldReturnGamesForMinPlayerCount() {
        List<BoardGame> result = recommendationService.findGamesByPlayerCount(2);

        assertEquals(2, result.size());
        assertEquals("7 Wonders", result.get(0).title());
        assertEquals("Azul", result.get(1).title());
    }

    @Test
    @DisplayName("findGamesByPlayerCount doit retourner les jeux pour le nombre maximum de joueurs")
    void findGamesByPlayerCount_shouldReturnGamesForMaxPlayerCount() {
        List<BoardGame> result = recommendationService.findGamesByPlayerCount(7);

        assertEquals(1, result.size());
        assertEquals("7 Wonders", result.get(0).title());
    }

    @Test
    @DisplayName("findGamesByPlayerCount doit retourner une liste vide pour un nombre de joueurs inférieur au minimum")
    void findGamesByPlayerCount_shouldReturnEmptyListForPlayerCountBelowMinimum() {
        List<BoardGame> result = recommendationService.findGamesByPlayerCount(1);

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("getRandomGames doit retourner le nombre de jeux demandé sans doublons")
    void getRandomGames_shouldReturnRequestedCountWithoutDuplicates() {
        List<BoardGame> result = recommendationService.getRandomGames(2);

        assertEquals(2, result.size());
        assertEquals(2, result.stream().distinct().count());
    }
}