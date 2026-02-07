package fr.fges.presentation;

import fr.fges.application.command.*;
import fr.fges.domain.service.GameService;
import fr.fges.exceptions.MenuExitException;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * Menu - Orchestrateur principal de l'interface utilisateur
 */
public class Menu {
    private final InputHandler inputHandler;
    private final MenuDisplay menuDisplay;
    private final Map<String, Command> commands;

    public Menu(GameService gameService) {
        this.inputHandler = new InputHandler();
        this.menuDisplay = isWeekend() ? new WeekendMenuDisplay() : new WeekdayMenuDisplay();
        this.commands = new HashMap<>();
        initializeCommands(gameService);
    }

    private void initializeCommands(GameService gameService) {
        commands.put("1", new AddGameCommand(gameService, inputHandler));
        commands.put("2", new RemoveGameCommand(gameService, inputHandler));
        commands.put("3", new ListGamesCommand(gameService));
        commands.put("4", new RecommendGameCommand(gameService, inputHandler));
        commands.put("5", new FindGamesByPlayerCountCommand(gameService, inputHandler));

        if (isWeekend()) {
            commands.put("6", new WeekendSummaryCommand(gameService));
            commands.put("7", new ExitCommand());
        } else {
            commands.put("6", new ExitCommand());
        }
    }

    public void handleMenu() throws MenuExitException {
        menuDisplay.display();
        String choice = inputHandler.getInput();
        Command command = commands.get(choice);

        if (command != null) {
            command.execute();
        } else {
            System.out.println("Invalid choice. Please try again.");
        }
    }

    private boolean isWeekend() {
        LocalDate today = LocalDate.now();
        DayOfWeek dayOfWeek = today.getDayOfWeek();
        return dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY;
    }

    public void close() {
        inputHandler.close();
    }
}
