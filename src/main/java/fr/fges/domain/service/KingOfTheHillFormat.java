package fr.fges.domain.service;

import fr.fges.domain.model.Match;
import fr.fges.domain.model.Player;
import fr.fges.domain.model.Tournament;

import java.util.List;

/**
 * KingOfTheHillFormat - Format "Roi de la colline"
 * Le gagnant reste en place, le perdant va en fin de file
 * Pour un nombre impair de joueurs, on fait une rotation simple
 */
public class KingOfTheHillFormat implements TournamentFormat {

    @Override
    public void generateMatches(Tournament tournament) {
        // Dans ce format, on ne génère pas tous les matchs à l'avance
        // On les crée au fur et à mesure
        List<Player> players = tournament.getPlayers();
        
        if (players.size() >= 2) {
            // Crée le premier match entre les deux premiers joueurs
            Match firstMatch = new Match(players.get(0), players.get(1));
            tournament.addMatch(firstMatch);
        }
    }

    @Override
    public Match getNextMatch(Tournament tournament) {
        List<Match> matches = tournament.getMatches();
        
        // Si aucun match n'a été joué, retourne le premier
        if (matches.isEmpty()) {
            return null;
        }
        
        // Trouve le dernier match
        Match lastMatch = matches.get(matches.size() - 1);
        
        // Si le dernier match n'est pas encore joué, on le retourne
        if (!lastMatch.isPlayed()) {
            return lastMatch;
        }
        
        // Si le dernier match est joué, on crée le suivant
        Player winner = lastMatch.getWinner();
        Player loser = lastMatch.getLoser();
        
        List<Player> players = tournament.getPlayers();
        
        // Le perdant va en fin de file, on fait une rotation simple
        // pour trouver le prochain adversaire qui n'est pas le roi (gagnant).
        int currentIndex = players.indexOf(loser);
        Player nextOpponent;
        
        do {
            currentIndex = (currentIndex + 1) % players.size();
            nextOpponent = players.get(currentIndex);
        } while (nextOpponent == winner);
        
        // Crée le prochain match
        Match newMatch = new Match(winner, nextOpponent);
        tournament.addMatch(newMatch);
        
        return newMatch;
    }
}
