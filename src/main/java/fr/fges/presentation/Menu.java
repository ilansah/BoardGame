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
    private final List<Command> commands;

    public Menu(GameService gameService) {
        this.inputHandler = new InputHandler();
        this.menuDisplay = isWeekend() ? new WeekendMenuDisplay() : new WeekdayMenuDisplay();
        this.commands = new ArrayList<>();
        initializeCommands(gameService);
    }

    private void initializeCommands(GameService gameService) {
        commands.add(new AddAction(gameService, inputHandler));
        commands.add(new RemoveGameCommand(gameService, inputHandler));
        commands.add(new ListGamesCommand(gameService));
        commands.add(new RecommendGameCommand(gameService, inputHandler));
        commands.add(new FindGamesByPlayerCountCommand(gameService, inputHandler));
        commands.add(new UndoCommand(gameService));

        if (isWeekend()) {
            commands.add(new WeekendSummaryCommand(gameService));
            commands.add(new ExitCommand());
        } else {
            commands.add(new ExitCommand());
        }
    }

    public void handleMenu() throws MenuExitException {
        System.out.println("Select an option:");
        for (int i = 0; i < commands.size(); i++) {
            System.out.println((i + 1) + ". " + commands.get(i).getName());
        }

        String choice = inputHandler.getInput();
        try {
            int index = Integer.parseInt(choice) - 1;
            if (index >= 0 && index < commands.size()) {
                commands.get(index).execute();
            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
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
