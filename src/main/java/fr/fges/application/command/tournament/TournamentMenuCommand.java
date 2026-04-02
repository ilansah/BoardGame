package fr.fges.application.command.tournament;

import fr.fges.application.command.Command;
import fr.fges.domain.model.BoardGame;
import fr.fges.domain.model.Tournament;
import fr.fges.domain.service.GameService;
import fr.fges.domain.service.TournamentService;
import fr.fges.presentation.InputHandler;
import fr.fges.presentation.TournamentFormatter;

import java.util.List;

/**
 * TournamentMenuCommand - Commande pour afficher le menu des tournois
 * Responsabilité: Afficher les options et créer/reprendre les sessions de tournoi
 */
public class TournamentMenuCommand implements Command {
    
    private final GameService gameService;
    private final TournamentService tournamentService;
    private final InputHandler inputHandler;

    public TournamentMenuCommand(GameService gameService, TournamentService tournamentService, InputHandler inputHandler) {
        this.gameService = gameService;
        this.tournamentService = tournamentService;
        this.inputHandler = inputHandler;
    }

    @Override
    public void execute() {
        boolean running = true;
        
        while (running) {
            try {
                running = handleMenu();
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
                createNewTournament();
                return true;
            }
            case "2" -> {
                if (tournamentService.hasTournament()) {
                    resumeTournamentSession();
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

    private void createNewTournament() {
        System.out.println("\n=== CRÉATION D'UN TOURNOI ===");
        
        System.out.print("Nom du tournoi: ");
        String name = inputHandler.getInput();
        
        List<BoardGame> twoPlayerGames = gameService.getAllGames().stream()
                .filter(g -> g.minPlayers() == 2 && g.maxPlayers() >= 2)
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
            launchTournamentSession();
        } catch (IllegalArgumentException e) {
            System.out.println("Erreur: " + e.getMessage());
        }
    }

    private void resumeTournamentSession() {
        launchTournamentSession();
    }

    private void launchTournamentSession() {
        TournamentSessionCommand session = new TournamentSessionCommand(tournamentService, inputHandler);
        session.execute();
    }

    private void showTournamentStatus() {
        Tournament tournament = tournamentService.getCurrentTournament();
        if (tournament == null) {
            System.out.println("Aucun tournoi en cours");
            return;
        }
        
        System.out.println("\n=== STATUT DU TOURNOI ===");
        System.out.println("Nom: " + tournament.getName());
        System.out.println("Jeu: " + tournament.getGame().title());
        System.out.println("Statut: " + tournament.getStatus());
        System.out.println("Joueurs: " + tournament.getPlayers().size());
    }

    @Override
    public String getName() {
        return "Tournament";
    }
}
