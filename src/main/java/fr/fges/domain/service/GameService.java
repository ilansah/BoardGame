package fr.fges.domain.service;

import fr.fges.domain.model.BoardGame;
import fr.fges.exceptions.DuplicateGameException;
import fr.fges.infrastructure.repository.GameRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * GameService - Service métier pour la gestion des jeux de société
 */
public class GameService {
    private final List<BoardGame> games;
    private final GameRepository repository;

    public GameService(GameRepository repository) {
        this.repository = repository;
        this.games = new ArrayList<>();
        loadGames();
    }

    private void loadGames() {
        List<BoardGame> loadedGames = repository.findAll();
        games.clear();
        games.addAll(loadedGames);
    }

    public void addGame(BoardGame game) {
        if (existsByTitle(game.title())) {
            throw new DuplicateGameException(game.title());
        }
        games.add(game);
        saveGames();
    }

    public void removeGame(String title) {
        games.removeIf(game -> game.title().equals(title));
        saveGames();
    }

    public List<BoardGame> getAllGames() {
        return new ArrayList<>(games);
    }

    public List<BoardGame> getSortedGames() {
        return games.stream()
                .sorted(Comparator.comparing(BoardGame::title))
                .toList();
    }

    public Optional<BoardGame> findByTitle(String title) {
        return games.stream()
                .filter(game -> game.title().equals(title))
                .findFirst();
    }

    public boolean isEmpty() {
        return games.isEmpty();
    }

    public boolean existsByTitle(String title) {
        return games.stream()
                .anyMatch(game -> game.title().equalsIgnoreCase(title));
    }

    public BoardGame recommendGame(int playerCount) {

        List<BoardGame> compatibleGames = games.stream()
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
        List<BoardGame> availableGames = new ArrayList<>(games);

        int actualCount = Math.min(count, availableGames.size());

        for (int i = 0; i < actualCount; i++) {
            int randomIndex = (int) (Math.random() * availableGames.size());

            result.add(availableGames.remove(randomIndex));
        }

        return result;
    }

    public List<BoardGame> findGamesByPlayerCount(int playerCount) {
        return games.stream()
                .filter(game -> game.minPlayers() <= playerCount && game.maxPlayers() >= playerCount)
                .sorted(Comparator.comparing(BoardGame::title))
                .toList();
    }

    private void saveGames() {
        repository.save(games);
    }
}
