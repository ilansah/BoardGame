package fr.fges.domain.service;

import fr.fges.domain.model.BoardGame;
import fr.fges.exceptions.DuplicateGameException;
import fr.fges.infrastructure.repository.GameRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * Service specialise pour la gestion CRUD des jeux.
 */
public class GameManagementService {
    private final List<BoardGame> games;
    private final GameRepository repository;

    public GameManagementService(GameRepository repository) {
        this.repository = repository;
        this.games = new ArrayList<>();
        loadGames();
    }

    public void addGame(BoardGame game) {
        if (existsByTitle(game.title())) {
            throw new DuplicateGameException(game.title());
        }

        games.add(game);
        saveGames();
    }

    public void removeGame(String title) {
        Optional<BoardGame> gameToRemove = findByTitle(title);
        if (gameToRemove.isPresent()) {
            games.removeIf(game -> game.title().equals(title));
            saveGames();
        }
    }

    public void addGameDirectly(BoardGame game) {
        games.add(game);
        saveGames();
    }

    public void removeGameDirectly(String title) {
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

    private void loadGames() {
        List<BoardGame> loadedGames = repository.findAll();
        games.clear();
        games.addAll(loadedGames);
    }

    private void saveGames() {
        repository.save(games);
    }
}