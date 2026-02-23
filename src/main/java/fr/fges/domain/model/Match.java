package fr.fges.domain.model;

/**
 * Match - Représente un match entre deux joueurs
 */
public class Match {
    private final Player player1;
    private final Player player2;
    private Player winner;
    private boolean isPlayed;

    public Match(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
        this.winner = null;
        this.isPlayed = false;
    }

    public void recordResult(String winnerName) {
        if (isPlayed) {
            throw new IllegalStateException("Ce match a déjà été joué");
        }

        if (winnerName.equalsIgnoreCase(player1.getName())) {
            winner = player1;
            player1.addWin();
            player2.addLoss();
        } else if (winnerName.equalsIgnoreCase(player2.getName())) {
            winner = player2;
            player2.addWin();
            player1.addLoss();
        } else {
            throw new IllegalArgumentException("Le gagnant doit être l'un des deux joueurs du match");
        }

        isPlayed = true;
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public Player getWinner() {
        return winner;
    }

    public Player getLoser() {
        if (!isPlayed || winner == null) {
            return null;
        }
        return winner == player1 ? player2 : player1;
    }

    public boolean isPlayed() {
        return isPlayed;
    }

    @Override
    public String toString() {
        if (isPlayed) {
            return player1.getName() + " vs " + player2.getName() + " → Winner: " + winner.getName();
        }
        return player1.getName() + " vs " + player2.getName() + " (à jouer)";
    }
}
