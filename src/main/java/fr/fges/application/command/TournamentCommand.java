package fr.fges.application.command;

import fr.fges.domain.model.BoardGame;
import fr.fges.domain.model.Match;
import fr.fges.domain.model.Player;
import fr.fges.domain.model.Tournament;
import fr.fges.domain.service.GameService;
import fr.fges.domain.service.TournamentService;
import fr.fges.presentation.InputHandler;
import fr.fges.presentation.TournamentFormatter;

import java.util.List;

/**
 * TournamentCommand - Commande pour gérer les tournois
 * Implémente une machine à états pour naviguer entre configuration et saisie des résultats
 */
public class TournamentCommand implements Command {
    
    private enum State {
        MENU,           // Menu principal du tournoi
        CONFIGURATION,  // Configuration du tournoi
        IN_PROGRESS,    // Tournoi en cours
        FINISHED        // Tournoi terminé
    }

    private final GameService gameService;
    private final TournamentService tournamentService;
    private final InputHandler inputHandler;
    private State currentState;

    public TournamentCommand(GameService gameService, TournamentService tournamentService, InputHandler inputHandler) {
        this.gameService = gameService;
        this.tournamentService = tournamentService;
        this.inputHandler = inputHandler;
        this.currentState = State.MENU;
    }

    @Override
    public void execute() {
        boolean running = true;
        
        while (running) {
            try {
                switch (currentState) {
                    case MENU -> running = handleMenu();
                    case CONFIGURATION -> handleConfiguration();
                    case IN_PROGRESS -> handleInProgress();
                    case FINISHED -> handleFinished();
                }
            } catch (Exception e) {
                System.out.println("Erreur: " + e.getMessage());
            }
        }
    }

    private boolean handleMenu() {
        System.out.println("\n=== MODE TOURNOI ===");
        System.out.println("1. Créer un nouveau tournoi");
        if (tournamentService.hasTournament()) {
            System.out.println("2. Reprendre le tournoi en cours");
            System.out.println("3. Voir le statut du tournoi");
        }
        System.out.println("0. Retour au menu principal");
        System.out.print("Choix: ");
        
        String choice = inputHandler.getInput();
        
        switch (choice) {
            case "1" -> {
                createTournament();
                return true;
            }
            case "2" -> {
                if (tournamentService.hasTournament()) {
                    resumeTournament();
                    return true;
                }
            }
            case "3" -> {
                if (tournamentService.hasTournament()) {
                    showTournamentStatus();
                }
                return true;
            }
            case "0" -> {
                return false;
            }
            default -> System.out.println("Choix invalide");
        }
        
        return true;
    }

    private void createTournament() {
        System.out.println("\n=== CRÉATION D'UN TOURNOI ===");
        
        // Nom du tournoi
        System.out.print("Nom du tournoi: ");
        String name = inputHandler.getInput();
        
        // Sélection du jeu (filtré pour 2 joueurs uniquement)
        List<BoardGame> twoPlayerGames = gameService.getAllGames().stream()
                .filter(g -> g.minPlayers() == 2 && g.maxPlayers() == 2)
                .toList();
        
        if (twoPlayerGames.isEmpty()) {
            System.out.println("Aucun jeu pour 2 joueurs disponible. Veuillez d'abord ajouter des jeux.");
            return;
        }
        
        System.out.println("\nJeux disponibles (2 joueurs):");
        for (int i = 0; i < twoPlayerGames.size(); i++) {
            BoardGame game = twoPlayerGames.get(i);
            System.out.println((i + 1) + ". " + game.title() + " (" + game.category() + ")");
        }
        
        System.out.print("Sélectionnez un jeu (numéro): ");
        int gameChoice;
        try {
            gameChoice = Integer.parseInt(inputHandler.getInput()) - 1;
            if (gameChoice < 0 || gameChoice >= twoPlayerGames.size()) {
                System.out.println("Choix invalide");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Entrée invalide");
            return;
        }
        
        BoardGame selectedGame = twoPlayerGames.get(gameChoice);
        
        // Format du tournoi
        System.out.println("\nFormat du tournoi:");
        System.out.println("1. Round Robin (tous contre tous)");
        System.out.println("2. King of the Hill (le gagnant reste)");
        System.out.print("Choix: ");
        
        Tournament.Format format;
        String formatChoice = inputHandler.getInput();
        if ("1".equals(formatChoice)) {
            format = Tournament.Format.ROUND_ROBIN;
        } else if ("2".equals(formatChoice)) {
            format = Tournament.Format.KING_OF_THE_HILL;
        } else {
            System.out.println("Choix invalide");
            return;
        }
        
        try {
            tournamentService.createTournament(name, selectedGame, format);
            System.out.println("\n✓ Tournoi créé avec succès!");
            currentState = State.CONFIGURATION;
        } catch (IllegalArgumentException e) {
            System.out.println("Erreur: " + e.getMessage());
        }
    }

    private void handleConfiguration() {
        Tournament tournament = tournamentService.getCurrentTournament();
        
        System.out.println("\n=== CONFIGURATION DU TOURNOI ===");
        System.out.println("Tournoi: " + tournament.getName());
        System.out.println("Jeu: " + tournament.getGame().title());
        
        TournamentFormatter.displayPlayers(tournament.getPlayers());
        
        System.out.println("\n1. Ajouter un joueur (3-8 joueurs requis)");
        System.out.println("2. Démarrer le tournoi");
        System.out.println("0. Annuler");
        System.out.print("Choix: ");
        
        String choice = inputHandler.getInput();
        
        switch (choice) {
            case "1" -> addPlayer();
            case "2" -> startTournament();
            case "0" -> {
                tournamentService.finishTournament();
                currentState = State.MENU;
            }
            default -> System.out.println("Choix invalide");
        }
    }

    private void addPlayer() {
        System.out.print("Nom du joueur: ");
        String playerName = inputHandler.getInput();
        
        try {
            tournamentService.addPlayer(playerName);
            System.out.println("✓ Joueur ajouté: " + playerName);
        } catch (Exception e) {
            System.out.println("Erreur: " + e.getMessage());
        }
    }

    private void startTournament() {
        try {
            tournamentService.startTournament();
            System.out.println("\n✓ Tournoi démarré!");
            currentState = State.IN_PROGRESS;
        } catch (IllegalStateException e) {
            System.out.println("Erreur: " + e.getMessage());
        }
    }

    private void handleInProgress() {
        Tournament tournament = tournamentService.getCurrentTournament();
        Match nextMatch = tournamentService.getNextMatch();
        
        if (nextMatch == null) {
            // Tournoi terminé
            currentState = State.FINISHED;
            handleFinished();
            return;
        }
        
        System.out.println("\n=== TOURNOI EN COURS ===");
        TournamentFormatter.displayMatch(nextMatch);
        
        System.out.println("\n1. Entrer le résultat du match");
        System.out.println("2. Voir le classement actuel");
        System.out.println("3. Voir tous les matchs");
        System.out.println("0. Retour au menu");
        System.out.print("Choix: ");
        
        String choice = inputHandler.getInput();
        
        switch (choice) {
            case "1" -> enterMatchResult(nextMatch);
            case "2" -> showRanking();
            case "3" -> TournamentFormatter.displayAllMatches(tournament.getMatches());
            case "0" -> currentState = State.MENU;
            default -> System.out.println("Choix invalide");
        }
    }

    private void enterMatchResult(Match match) {
        System.out.println("\nQui a gagné?");
        System.out.println("1. " + match.getPlayer1().getName());
        System.out.println("2. " + match.getPlayer2().getName());
        System.out.print("Choix: ");
        
        String choice = inputHandler.getInput();
        String winnerName;
        
        if ("1".equals(choice)) {
            winnerName = match.getPlayer1().getName();
        } else if ("2".equals(choice)) {
            winnerName = match.getPlayer2().getName();
        } else {
            System.out.println("Choix invalide");
            return;
        }
        
        try {
            tournamentService.recordMatchResult(match, winnerName);
            System.out.println("\n✓ Résultat enregistré!");
            
            // Vérifie si le tournoi est terminé
            if (tournamentService.isFinished()) {
                currentState = State.FINISHED;
            }
        } catch (Exception e) {
            System.out.println("Erreur: " + e.getMessage());
        }
    }

    private void handleFinished() {
        Tournament tournament = tournamentService.getCurrentTournament();
        List<Player> ranking = tournamentService.getRanking();
        Player champion = tournamentService.getChampion();
        
        System.out.println("\n=== TOURNOI TERMINÉ ===");
        TournamentFormatter.displayChampion(champion);
        TournamentFormatter.displayRanking(ranking);
        
        System.out.println("\n1. Voir tous les matchs");
        System.out.println("0. Retour au menu");
        System.out.print("Choix: ");
        
        String choice = inputHandler.getInput();
        
        if ("1".equals(choice)) {
            TournamentFormatter.displayAllMatches(tournament.getMatches());
        } else if ("0".equals(choice)) {
            tournamentService.finishTournament();
            currentState = State.MENU;
        }
    }

    private void resumeTournament() {
        Tournament tournament = tournamentService.getCurrentTournament();
        
        if (tournament.getStatus() == Tournament.Status.CONFIGURATION) {
            currentState = State.CONFIGURATION;
        } else if (tournament.getStatus() == Tournament.Status.IN_PROGRESS) {
            currentState = State.IN_PROGRESS;
        } else {
            currentState = State.FINISHED;
        }
    }

    private void showTournamentStatus() {
        Tournament tournament = tournamentService.getCurrentTournament();
        TournamentFormatter.displayTournamentStatus(tournament);
        showRanking();
    }

    private void showRanking() {
        List<Player> ranking = tournamentService.getRanking();
        TournamentFormatter.displayRanking(ranking);
    }
}
