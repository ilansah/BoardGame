package fr.fges.domain.service;

import fr.fges.domain.model.BoardGame;

/**
 * Port metier minimal pour appliquer des mutations sans enregistrer d'action historique.
 */
public interface GameMutationPort {
    void addGameDirectly(BoardGame game);

    void removeGameDirectly(String title);
}