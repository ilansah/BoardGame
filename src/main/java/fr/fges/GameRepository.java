package fr.fges;

import java.util.List;

public interface GameRepository {
    List<BoardGame> findAll();

    void save(List<BoardGame> games);
}
