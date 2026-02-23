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
        
        // Trouve le prochain adversaire dans la liste des joueurs
        List<Player> players = tournament.getPlayers();
        
        // On cherche le joueur qui n'a pas encore affronté le gagnant
        // ou qui a le moins joué
        Player nextOpponent = findNextOpponent(tournament, winner, players);
        
        if (nextOpponent == null) {
            // Tous les joueurs ont suffisamment joué
            return null;
        }
        
        // Crée le prochain match
        Match newMatch = new Match(winner, nextOpponent);
        tournament.addMatch(newMatch);
        
        return newMatch;
    }

    private Player findNextOpponent(Tournament tournament, Player currentKing, List<Player> allPlayers) {
        List<Match> matches = tournament.getMatches();
        
        // Compte le nombre de matchs joués par chaque joueur
        for (Player player : allPlayers) {
            if (player == currentKing) {
                continue; // Le roi ne peut pas jouer contre lui-même
            }
            
            // Vérifie si ce joueur a déjà joué contre le roi
            boolean hasPlayedAgainstKing = matches.stream()
                    .filter(Match::isPlayed)
                    .anyMatch(m -> 
                        (m.getPlayer1() == currentKing && m.getPlayer2() == player) ||
                        (m.getPlayer2() == currentKing && m.getPlayer1() == player)
                    );
            
            // Si ce joueur n'a pas encore affronté le roi, c'est lui le prochain
            if (!hasPlayedAgainstKing) {
                return player;
            }
        }
        
        // Si tous les joueurs ont déjà affronté le roi, on prend le joueur
        // avec le moins de matchs joués (hors le roi)
        return allPlayers.stream()
                .filter(p -> p != currentKing)
                .min((p1, p2) -> {
                    long p1Matches = countPlayerMatches(matches, p1);
                    long p2Matches = countPlayerMatches(matches, p2);
                    return Long.compare(p1Matches, p2Matches);
                })
                .orElse(null);
    }

    private long countPlayerMatches(List<Match> matches, Player player) {
        return matches.stream()
                .filter(Match::isPlayed)
                .filter(m -> m.getPlayer1() == player || m.getPlayer2() == player)
                .count();
    }

    @Override
    public String getFormatName() {
        return "King of the Hill (le gagnant reste)";
    }
}
