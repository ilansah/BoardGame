package fr.fges.domain.service;

import fr.fges.domain.model.BoardGame;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Service specialise pour les recommandations et recherches de jeux.
 */
public class GameRecommendationService {
    private final GameManagementService gameManagementService;

    public GameRecommendationService(GameManagementService gameManagementService) {
        this.gameManagementService = gameManagementService;
    }

    public BoardGame recommendGame(int playerCount) {
        List<BoardGame> compatibleGames = gameManagementService.getAllGames().stream()
                .filter(game -> game.minPlayers() <= playerCount && game.maxPlayers() >= playerCount)
                .toList();

        if (compatibleGames.isEmpty()) {
            return null;
        }

        int randomIndex = (int) (Math.random() * compatibleGames.size());
        return compatibleGames.get(randomIndex);
    }

    public List<BoardGame> getRandomGames(int count) {
        List<BoardGame> result = new ArrayList<>();
        List<BoardGame> availableGames = new ArrayList<>(gameManagementService.getAllGames());

        int actualCount = Math.min(count, availableGames.size());

        for (int i = 0; i < actualCount; i++) {
            int randomIndex = (int) (Math.random() * availableGames.size());
            result.add(availableGames.remove(randomIndex));
        }

        return result;
    }

    public List<BoardGame> findGamesByPlayerCount(int playerCount) {
        return gameManagementService.getAllGames().stream()
                .filter(game -> game.minPlayers() <= playerCount && game.maxPlayers() >= playerCount)
                .sorted(Comparator.comparing(BoardGame::title))
                .toList();
    }
}