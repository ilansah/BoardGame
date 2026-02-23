package fr.fges.domain.model;

/**
 * Player - Représente un joueur dans un tournoi
 */
public class Player {
    private final String name;
    private int wins;
    private int losses;
    private int points;

    public Player(String name) {
        this.name = name;
        this.wins = 0;
        this.losses = 0;
        this.points = 0;
    }

    public void addWin() {
        this.wins++;
        this.points += 3; // Victoire = 3 points
    }

    public void addLoss() {
        this.losses++;
        this.points += 1; // Défaite = 1 point
    }

    public String getName() {
        return name;
    }

    public int getWins() {
        return wins;
    }

    public int getLosses() {
        return losses;
    }

    public int getPoints() {
        return points;
    }

    public void resetStats() {
        this.wins = 0;
        this.losses = 0;
        this.points = 0;
    }

    @Override
    public String toString() {
        return name + " (W:" + wins + " L:" + losses + " Pts:" + points + ")";
    }
}
