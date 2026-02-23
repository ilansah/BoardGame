package fr.fges.presentation;

import fr.fges.domain.model.Match;
import fr.fges.domain.model.Player;
import fr.fges.domain.model.Tournament;

import java.util.List;

/**
 * TournamentFormatter - Utilitaire pour formater l'affichage des tournois
 */
public class TournamentFormatter {

    /**
     * Affiche le statut actuel du tournoi
     */
    public static void displayTournamentStatus(Tournament tournament) {
        System.out.println("\n╔══════════════════════════════════════════════╗");
        System.out.println("║          STATUT DU TOURNOI                   ║");
        System.out.println("╚══════════════════════════════════════════════╝");
        System.out.println("Nom: " + tournament.getName());
        System.out.println("Jeu: " + tournament.getGame().title());
        System.out.println("Format: " + getFormatName(tournament.getFormat()));
        System.out.println("Statut: " + getStatusName(tournament.getStatus()));
        System.out.println("Joueurs: " + tournament.getPlayers().size());
        System.out.println("Matchs joués: " + tournament.getPlayedMatchesCount() + "/" + tournament.getTotalMatchesCount());
    }

    /**
     * Affiche le classement des joueurs
     */
    public static void displayRanking(List<Player> ranking) {
        System.out.println("\n┌─────────────────────────────────────────────┐");
        System.out.println("│            CLASSEMENT ACTUEL                │");
        System.out.println("├──────┬─────────────────┬──────┬──────┬──────┤");
        System.out.println("│ Rang │ Joueur          │ Pts  │ V    │ D    │");
        System.out.println("├──────┼─────────────────┼──────┼──────┼──────┤");
        
        int rank = 1;
        for (Player player : ranking) {
            System.out.printf("│ %-4d │ %-15s │ %-4d │ %-4d │ %-4d │%n",
                    rank++,
                    truncate(player.getName(), 15),
                    player.getPoints(),
                    player.getWins(),
                    player.getLosses());
        }
        
        System.out.println("└──────┴─────────────────┴──────┴──────┴──────┘");
    }

    /**
     * Affiche un match
     */
    public static void displayMatch(Match match) {
        System.out.println("\n━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        if (match.isPlayed()) {
            System.out.println("  " + match.getPlayer1().getName() + " vs " + match.getPlayer2().getName());
            System.out.println("  Gagnant: " + match.getWinner().getName() + " ✓");
        } else {
            System.out.println("  " + match.getPlayer1().getName() + " vs " + match.getPlayer2().getName());
            System.out.println("  (Match à jouer)");
        }
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
    }

    /**
     * Affiche le champion
     */
    public static void displayChampion(Player champion) {
        System.out.println("\n");
        System.out.println("╔═════════════════════════════════════════════╗");
        System.out.println("║          🏆  CHAMPION  🏆                   ║");
        System.out.println("╠═════════════════════════════════════════════╣");
        System.out.println("║  " + String.format("%-41s", champion.getName()) + "  ║");
        System.out.println("║  Points: " + String.format("%-32s", champion.getPoints()) + "  ║");
        System.out.println("║  Victoires: " + String.format("%-29s", champion.getWins()) + "  ║");
        System.out.println("╚═════════════════════════════════════════════╝");
    }

    /**
     * Affiche la liste des joueurs
     */
    public static void displayPlayers(List<Player> players) {
        System.out.println("\nJoueurs inscrits (" + players.size() + "):");
        for (int i = 0; i < players.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + players.get(i).getName());
        }
    }

    /**
     * Affiche tous les matchs
     */
    public static void displayAllMatches(List<Match> matches) {
        System.out.println("\n=== HISTORIQUE DES MATCHS ===");
        for (int i = 0; i < matches.size(); i++) {
            Match m = matches.get(i);
            System.out.print("Match " + (i + 1) + ": " + m.getPlayer1().getName() + " vs " + m.getPlayer2().getName());
            if (m.isPlayed()) {
                System.out.println(" → Gagnant: " + m.getWinner().getName());
            } else {
                System.out.println(" (à jouer)");
            }
        }
    }

    private static String getFormatName(Tournament.Format format) {
        return switch (format) {
            case ROUND_ROBIN -> "Round Robin (tous contre tous)";
            case KING_OF_THE_HILL -> "King of the Hill (le gagnant reste)";
        };
    }

    private static String getStatusName(Tournament.Status status) {
        return switch (status) {
            case CONFIGURATION -> "Configuration";
            case IN_PROGRESS -> "En cours";
            case FINISHED -> "Terminé";
        };
    }

    private static String truncate(String str, int maxLength) {
        if (str.length() <= maxLength) {
            return str;
        }
        return str.substring(0, maxLength - 3) + "...";
    }
}
