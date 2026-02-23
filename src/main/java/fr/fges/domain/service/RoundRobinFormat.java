package fr.fges.domain.service;

import fr.fges.domain.model.Match;
import fr.fges.domain.model.Player;
import fr.fges.domain.model.Tournament;

import java.util.List;

/**
 * RoundRobinFormat - Format "tous contre tous"
 * Chaque joueur affronte tous les autres une fois
 */
public class RoundRobinFormat implements TournamentFormat {

    @Override
    public void generateMatches(Tournament tournament) {
        List<Player> players = tournament.getPlayers();
        
        // Génère toutes les paires possibles
        for (int i = 0; i < players.size(); i++) {
            for (int j = i + 1; j < players.size(); j++) {
                Match match = new Match(players.get(i), players.get(j));
                tournament.addMatch(match);
            }
        }
    }

    @Override
    public Match getNextMatch(Tournament tournament) {
        // Retourne le premier match non joué
        return tournament.getMatches().stream()
                .filter(m -> !m.isPlayed())
                .findFirst()
                .orElse(null);
    }

    @Override
    public String getFormatName() {
        return "Round Robin (tous contre tous)";
    }
}
