package fr.fges.presentation;

import fr.fges.application.command.*;
import fr.fges.domain.service.GameService;
import fr.fges.domain.service.TournamentService;
import fr.fges.exceptions.MenuExitException;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Menu - Orchestrateur principal de l'interface utilisateur
 */
public class Menu {
    private final InputHandler inputHandler;
    private final MenuDisplay menuDisplay;
    private final List<Command> commands;

    public Menu(GameService gameService, TournamentService tournamentService) {
        this.inputHandler = new InputHandler();
        this.menuDisplay = new MenuDisplay();
        this.commands = new ArrayList<>();
        
        commands.add(new AddAction(gameService, inputHandler));
        commands.add(new RemoveGameCommand(gameService, inputHandler));
        commands.add(new ListGamesCommand(gameService));
        commands.add(new RecommendGameCommand(gameService, inputHandler));
        commands.add(new FindGamesByPlayerCountCommand(gameService, inputHandler));
        commands.add(new UndoCommand(gameService));
        commands.add(new TournamentCommand(gameService, tournamentService, inputHandler));
        commands.add(new WeekendSummaryCommand(gameService));
        commands.add(new ExitCommand());
    }

    public void handleMenu() throws MenuExitException {
        menuDisplay.display(commands);

        String choice = inputHandler.getInput();
        try {
            int option = Integer.parseInt(choice);
            if (option >= 1 && option <= commands.size()) {
                commands.get(option - 1).execute();
            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
        }
    }

    public void close() {
        inputHandler.close();
    }
}
