package fr.fges.domain.service;

import fr.fges.domain.model.BoardGame;
import fr.fges.domain.port.GameRepository;
import fr.fges.exceptions.DuplicateGameException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * Service specialise pour la gestion CRUD des jeux.
 */
public class GameManagementService {
    private final GameRepository repository;

    public GameManagementService(GameRepository repository) {
        this.repository = repository;
        // Validation d'acces repository au demarrage, sans conserver de cache en RAM.
        loadGames();
    }

    public void addGame(BoardGame game) {
        List<BoardGame> games = loadGames();

        if (games.stream().anyMatch(existing -> existing.title().equalsIgnoreCase(game.title()))) {
            throw new DuplicateGameException(game.title());
        }

        games.add(game);
        repository.save(games);
    }

    public void removeGame(String title) {
        List<BoardGame> games = loadGames();
        boolean removed = games.removeIf(game -> game.title().equals(title));

        if (removed) {
            repository.save(games);
        }
    }

    public void addGameDirectly(BoardGame game) {
        List<BoardGame> games = loadGames();
        games.add(game);
        repository.save(games);
    }

    public void removeGameDirectly(String title) {
        List<BoardGame> games = loadGames();
        boolean removed = games.removeIf(game -> game.title().equals(title));

        if (removed) {
            repository.save(games);
        }
    }

    public List<BoardGame> getAllGames() {
        return loadGames();
    }

    public List<BoardGame> getSortedGames() {
        return loadGames().stream()
                .sorted(Comparator.comparing(BoardGame::title))
                .toList();
    }

    public Optional<BoardGame> findByTitle(String title) {
        return loadGames().stream()
                .filter(game -> game.title().equals(title))
                .findFirst();
    }

    public boolean isEmpty() {
        return loadGames().isEmpty();
    }

    public boolean existsByTitle(String title) {
        return loadGames().stream()
                .anyMatch(game -> game.title().equalsIgnoreCase(title));
    }

    private List<BoardGame> loadGames() {
        return new ArrayList<>(repository.findAll());
    }
}