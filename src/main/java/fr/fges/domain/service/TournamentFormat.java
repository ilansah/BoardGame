package fr.fges.domain.service;

import fr.fges.domain.model.Match;
import fr.fges.domain.model.Player;
import fr.fges.domain.model.Tournament;

import java.util.List;

/**
 * TournamentFormat - Interface pour les différents formats de tournoi
 */
public interface TournamentFormat {
    /**
     * Génère les matchs pour le tournoi selon le format
     * @param tournament Le tournoi
     */
    void generateMatches(Tournament tournament);

    /**
     * Détermine le prochain match à jouer selon le format
     * @param tournament Le tournoi
     * @return Le prochain match à jouer, ou null si tous les matchs sont joués
     */
    Match getNextMatch(Tournament tournament);

}
