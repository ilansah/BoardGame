package fr.fges.infrastructure.repository;

import fr.fges.domain.model.BoardGame;

import java.util.List;

/**
 * GameRepository - Interface pour la persistence des jeux
 */
public interface GameRepository {

    List<BoardGame> findAll();

    void save(List<BoardGame> games);
}
