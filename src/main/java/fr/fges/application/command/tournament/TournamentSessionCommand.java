package fr.fges.application.command.tournament;

import fr.fges.domain.model.Match;
import fr.fges.domain.model.Player;
import fr.fges.domain.model.Tournament;
import fr.fges.domain.service.TournamentService;
import fr.fges.presentation.InputHandler;
import fr.fges.presentation.TournamentFormatter;

import java.util.List;

/**
 * TournamentSessionCommand - Commande pour gérer une session de tournoi active
 * Responsabilité: Gérer configuration, jeu et résultats d'un tournoi en cours
 */
class TournamentSessionCommand {
    
    private enum SessionState {
        CONFIGURATION,
        IN_PROGRESS,
        FINISHED
    }

    private final TournamentService tournamentService;
    private final InputHandler inputHandler;
    private SessionState currentState;

    public TournamentSessionCommand(TournamentService tournamentService, InputHandler inputHandler) {
        this.tournamentService = tournamentService;
        this.inputHandler = inputHandler;
        this.currentState = determineInitialState();
    }

    public void execute() {
        boolean running = true;
        
        while (running) {
            try {
                switch (currentState) {
                    case CONFIGURATION -> handleConfiguration();
                    case IN_PROGRESS -> handleInProgress();
                    case FINISHED -> running = handleFinished();
                }
            } catch (Exception e) {
                System.out.println("Erreur: " + e.getMessage());
            }
        }
    }

    private SessionState determineInitialState() {
        Tournament tournament = tournamentService.getCurrentTournament();
        if (tournament == null) return SessionState.CONFIGURATION;
        
        if (tournament.getStatus() == Tournament.Status.CONFIGURATION) {
            return SessionState.CONFIGURATION;
        } else if (tournament.getStatus() == Tournament.Status.IN_PROGRESS) {
            return SessionState.IN_PROGRESS;
        } else {
            return SessionState.FINISHED;
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
        System.out.println("0. Annuler et retour au menu");
        System.out.print("Choix: ");
        
        String choice = inputHandler.getInput();
        
        switch (choice) {
            case "1" -> addPlayer();
            case "2" -> startTournament();
            case "0" -> {
                tournamentService.finishTournament();
                currentState = SessionState.FINISHED;
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
            currentState = SessionState.IN_PROGRESS;
        } catch (IllegalStateException e) {
            System.out.println("Erreur: " + e.getMessage());
        }
    }

    private void handleInProgress() {
        Tournament tournament = tournamentService.getCurrentTournament();
        Match nextMatch = tournamentService.getNextMatch();
        
        if (nextMatch == null) {
            currentState = SessionState.FINISHED;
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
            case "0" -> currentState = SessionState.FINISHED;
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
            
            if (tournamentService.isFinished()) {
                currentState = SessionState.FINISHED;
            }
        } catch (Exception e) {
            System.out.println("Erreur: " + e.getMessage());
        }
    }

    private void showRanking() {
        List<Player> ranking = tournamentService.getRanking();
        TournamentFormatter.displayRanking(ranking);
    }

    private boolean handleFinished() {
        Tournament tournament = tournamentService.getCurrentTournament();
        List<Player> ranking = tournamentService.getRanking();
        Player champion = tournamentService.getChampion();
        
        System.out.println("\n=== TOURNOI TERMINÉ ===");
        TournamentFormatter.displayChampion(champion);
        TournamentFormatter.displayRanking(ranking);
        
        System.out.println("\n1. Voir tous les matchs");
        System.out.println("0. Retour au menu principal");
        System.out.print("Choix: ");
        
        String choice = inputHandler.getInput();
        
        if ("1".equals(choice)) {
            TournamentFormatter.displayAllMatches(tournament.getMatches());
            return true;
        } else if ("0".equals(choice)) {
            tournamentService.finishTournament();
            return false;
        } else {
            System.out.println("Choix invalide");
            return true;
        }
    }
}
