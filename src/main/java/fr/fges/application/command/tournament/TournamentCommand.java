package fr.fges.application.command.tournament;

import fr.fges.application.command.Command;
import fr.fges.domain.service.GameService;
import fr.fges.domain.service.TournamentService;
import fr.fges.presentation.InputHandler;

/**
 * TournamentCommand - Point d'entrée pour le mode tournoi
 * Délègue au TournamentMenuCommand pour le traitement
 */
public class TournamentCommand implements Command {

    private final GameService gameService;
    private final TournamentService tournamentService;
    private final InputHandler inputHandler;

    public TournamentCommand(GameService gameService, TournamentService tournamentService, InputHandler inputHandler) {
        this.gameService = gameService;
        this.tournamentService = tournamentService;
        this.inputHandler = inputHandler;
    }

    @Override
    public void execute() {
        TournamentMenuCommand menuCommand = new TournamentMenuCommand(gameService, tournamentService, inputHandler);
        menuCommand.execute();
    }

    @Override
    public String getName() {
        return "Tournament Mode";
    }
}