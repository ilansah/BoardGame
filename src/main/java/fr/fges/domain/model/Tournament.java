package fr.fges.domain.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Tournament - Représente un tournoi avec ses participants et ses matchs
 */
public class Tournament {
    public enum Status {
        CONFIGURATION,  // En cours de configuration
        IN_PROGRESS,     // Tournoi en cours
        FINISHED         // Tournoi terminé
    }

    public enum Format {
        ROUND_ROBIN,      // Tous contre tous
        KING_OF_THE_HILL  // Le gagnant reste en place
    }

    private final String id;
    private final String name;
    private final BoardGame game;
    private final List<Player> players;
    private final List<Match> matches;
    private final Format format;
    private Status status;

    public Tournament(String name, BoardGame game, Format format) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.game = game;
        this.format = format;
        this.players = new ArrayList<>();
        this.matches = new ArrayList<>();
        this.status = Status.CONFIGURATION;
    }

    public void addPlayer(String playerName) {
        if (status != Status.CONFIGURATION) {
            throw new IllegalStateException("Cannot add players after tournament has started");
        }
        if (players.size() >= 8) {
            throw new IllegalStateException("Maximum 8 players allowed");
        }
        if (players.stream().anyMatch(p -> p.getName().equalsIgnoreCase(playerName))) {
            throw new IllegalArgumentException("Player already exists: " + playerName);
        }
        players.add(new Player(playerName));
    }

    public void start() {
        if (status != Status.CONFIGURATION) {
            throw new IllegalStateException("Tournament has already started");
        }
        if (players.size() < 3) {
            throw new IllegalStateException("Minimum 3 players required");
        }
        status = Status.IN_PROGRESS;
    }

    public void finish() {
        if (status != Status.IN_PROGRESS) {
            throw new IllegalStateException("Tournament is not in progress");
        }
        status = Status.FINISHED;
    }

    public void addMatch(Match match) {
        matches.add(match);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BoardGame getGame() {
        return game;
    }

    public List<Player> getPlayers() {
        return new ArrayList<>(players);
    }

    public List<Match> getMatches() {
        return new ArrayList<>(matches);
    }

    public Format getFormat() {
        return format;
    }

    public Status getStatus() {
        return status;
    }

    public long getPlayedMatchesCount() {
        return matches.stream().filter(Match::isPlayed).count();
    }

    public long getTotalMatchesCount() {
        return matches.size();
    }
}
