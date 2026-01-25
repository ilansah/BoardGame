package fr.fges;

import java.util.Comparator;
import java.util.List;

public class GameService {
    private final GameRepository repository;
    private final List<BoardGame> games;

    public GameService(GameRepository repository) {
        this.repository = repository;
        this.games = repository.findAll();
    }

    public void addGame(BoardGame game) {
        games.add(game);
        repository.save(games);
    }

    public void removeGame(String title) {
        games.removeIf(game -> game.title().equals(title));
        repository.save(games);
    }

    public List<BoardGame> getSortedGames() {
        return games.stream()
                .sorted(Comparator.comparing(BoardGame::title))
                .toList();
    }
}
