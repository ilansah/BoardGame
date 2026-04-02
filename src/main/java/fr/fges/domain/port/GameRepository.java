package fr.fges.domain.port;

import fr.fges.domain.model.BoardGame;

import java.util.List;

/**
 * Port metier pour la persistence des jeux.
 */
public interface GameRepository {

    List<BoardGame> findAll();

    void save(List<BoardGame> games);
}