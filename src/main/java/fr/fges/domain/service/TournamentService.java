package fr.fges.domain.service;

import fr.fges.domain.model.BoardGame;
import fr.fges.domain.model.Match;
import fr.fges.domain.model.Player;
import fr.fges.domain.model.Tournament;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * TournamentService - Service métier pour la gestion des tournois
 */
public class TournamentService {
    private Tournament currentTournament;
    private TournamentFormat currentFormat;

    public TournamentService() {
        this.currentTournament = null;
        this.currentFormat = null;
    }

    /**
     * Crée un nouveau tournoi
     * @param name Le nom du tournoi
     * @param game Le jeu (doit être pour 2 joueurs)
     * @param format Le format du tournoi
     * @throws IllegalArgumentException si le jeu n'est pas pour 2 joueurs
     */
    public void createTournament(String name, BoardGame game, Tournament.Format format) {
        // Validation : le jeu doit être strictement pour 2 joueurs
        if (game.minPlayers() != 2 || game.maxPlayers() != 2) {
            throw new IllegalArgumentException(
                "Le jeu doit être strictement pour 2 joueurs. " +
                game.title() + " est pour " + game.minPlayers() + "-" + game.maxPlayers() + " joueurs."
            );
        }

        this.currentTournament = new Tournament(name, game, format);
        
        // Sélectionne le format approprié
        this.currentFormat = switch (format) {
            case ROUND_ROBIN -> new RoundRobinFormat();
            case KING_OF_THE_HILL -> new KingOfTheHillFormat();
        };
    }

    /**
     * Ajoute un joueur au tournoi
     * @param playerName Le nom du joueur
     * @throws IllegalStateException si aucun tournoi n'est actif ou si le tournoi a déjà démarré
     */
    public void addPlayer(String playerName) {
        if (currentTournament == null) {
            throw new IllegalStateException("Aucun tournoi actif");
        }
        currentTournament.addPlayer(playerName);
    }

    /**
     * Démarre le tournoi et génère les matchs
     * @throws IllegalStateException si le nombre de joueurs n'est pas valide (3-8)
     */
    public void startTournament() {
        if (currentTournament == null) {
            throw new IllegalStateException("Aucun tournoi actif");
        }
        
        int playerCount = currentTournament.getPlayers().size();
        if (playerCount < 3 || playerCount > 8) {
            throw new IllegalStateException("Le nombre de participants doit être entre 3 et 8 (actuel: " + playerCount + ")");
        }

        currentTournament.start();
        currentFormat.generateMatches(currentTournament);
    }

    /**
     * Enregistre le résultat d'un match
     * @param match Le match concerné
     * @param winnerName Le nom du gagnant
     */
    public void recordMatchResult(Match match, String winnerName) {
        if (currentTournament == null) {
            throw new IllegalStateException("Aucun tournoi actif");
        }
        if (currentTournament.getStatus() != Tournament.Status.IN_PROGRESS) {
            throw new IllegalStateException("Le tournoi n'est pas en cours");
        }
        
        match.recordResult(winnerName);
        
        // Vérifie si le tournoi est terminé
        if (isFinished()) {
            currentTournament.finish();
        }
    }

    /**
     * Retourne le prochain match à jouer
     */
    public Match getNextMatch() {
        if (currentTournament == null || currentFormat == null) {
            return null;
        }
        return currentFormat.getNextMatch(currentTournament);
    }

    /**
     * Vérifie si le tournoi est terminé
     */
    public boolean isFinished() {
        if (currentTournament == null) {
            return false;
        }
        
        Match nextMatch = currentFormat.getNextMatch(currentTournament);
        return nextMatch == null;
    }

    /**
     * Retourne le classement actuel des joueurs
     * Tri par points (décroissant), puis par victoires, puis par nom (alphabétique)
     */
    public List<Player> getRanking() {
        if (currentTournament == null) {
            return List.of();
        }
        
        return currentTournament.getPlayers().stream()
                .sorted(Comparator
                        .comparingInt(Player::getPoints).reversed()
                        .thenComparingInt(Player::getWins).reversed()
                        .thenComparing(Player::getName))
                .collect(Collectors.toList());
    }

    /**
     * Retourne le champion (joueur avec le plus de points)
     */
    public Player getChampion() {
        List<Player> ranking = getRanking();
        return ranking.isEmpty() ? null : ranking.get(0);
    }

    /**
     * Retourne le tournoi actuel
     */
    public Tournament getCurrentTournament() {
        return currentTournament;
    }

    /**
     * Vérifie si un tournoi est en cours
     */
    public boolean hasTournament() {
        return currentTournament != null;
    }

    /**
     * Termine le tournoi actuel
     */
    public void finishTournament() {
        if (currentTournament != null) {
            currentTournament = null;
            currentFormat = null;
        }
    }
}
