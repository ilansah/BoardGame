package fr.fges.domain.service;

import fr.fges.domain.model.Action;
import fr.fges.domain.model.BoardGame;
import fr.fges.infrastructure.repository.GameRepository;

import java.util.List;
import java.util.Optional;

/**
 * GameService - Service métier pour la gestion des jeux de société
 */
public class GameService {
    private final GameManagementService gameManagementService;
    private final GameRecommendationService gameRecommendationService;
    private final GameHistoryService gameHistoryService;

    public GameService(GameRepository repository) {
        this.gameManagementService = new GameManagementService(repository);
        this.gameRecommendationService = new GameRecommendationService(gameManagementService);
        this.gameHistoryService = new GameHistoryService();
    }


    public void addGame(BoardGame game) {
        gameManagementService.addGame(game);
        gameHistoryService.recordAddAction(game, this);
    }


    public void removeGame(String title) {
        Optional<BoardGame> gameToRemove = gameManagementService.findByTitle(title);

        if (gameToRemove.isPresent()) {
            BoardGame game = gameToRemove.get();
            gameManagementService.removeGame(title);
            gameHistoryService.recordRemoveAction(game, this);
        }
    }


    public void addGameDirectly(BoardGame game) {
        gameManagementService.addGameDirectly(game);
    }

 
    public void removeGameDirectly(String title) {
        gameManagementService.removeGameDirectly(title);
    }

    public List<BoardGame> getAllGames() {
        return gameManagementService.getAllGames();
    }

    public List<BoardGame> getSortedGames() {
        return gameManagementService.getSortedGames();
    }

    public Optional<BoardGame> findByTitle(String title) {
        return gameManagementService.findByTitle(title);
    }

    public boolean isEmpty() {
        return gameManagementService.isEmpty();
    }

    public boolean existsByTitle(String title) {
        return gameManagementService.existsByTitle(title);
    }

    public BoardGame recommendGame(int playerCount) {
        return gameRecommendationService.recommendGame(playerCount);
    }


    public boolean undo() {
        return gameHistoryService.undo();
    }

 
    public boolean hasActionsToUndo() {
        return gameHistoryService.hasActionsToUndo();
    }


    public Action getLastAction() {
        return gameHistoryService.getLastAction();
    }

    public List<BoardGame> getRandomGames(int count) {
        return gameRecommendationService.getRandomGames(count);
    }

    public List<BoardGame> findGamesByPlayerCount(int playerCount) {
        return gameRecommendationService.findGamesByPlayerCount(playerCount);
    }
}
