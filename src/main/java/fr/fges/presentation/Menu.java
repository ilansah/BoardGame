package fr.fges.presentation;

import fr.fges.application.command.*;
import fr.fges.domain.service.GameService;
import fr.fges.exceptions.MenuExitException;

import java.time.DayOfWeek;
import java.time.LocalDate;

/**
 * Menu - Orchestrateur principal de l'interface utilisateur
 */
public class Menu {
    private final InputHandler inputHandler;
    private final MenuDisplay menuDisplay;
    private final GameService gameService;

    public Menu(GameService gameService) {
        this.inputHandler = new InputHandler();
        this.menuDisplay = new MenuDisplay();
        this.gameService = gameService;
    }

    public void handleMenu() throws MenuExitException {
        boolean isWeekend = isWeekend();
        menuDisplay.display(isWeekend);

        String choice = inputHandler.getInput();
        try {
            int option = Integer.parseInt(choice);
            executeCommand(option, isWeekend);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
        }
    }

    private void executeCommand(int option, boolean isWeekend) throws MenuExitException {
        switch (option) {
            case 1 -> new AddAction(gameService, inputHandler).execute();
            case 2 -> new RemoveGameCommand(gameService, inputHandler).execute();
            case 3 -> new ListGamesCommand(gameService).execute();
            case 4 -> new RecommendGameCommand(gameService, inputHandler).execute();
            case 5 -> new FindGamesByPlayerCountCommand(gameService, inputHandler).execute();
            case 6 -> new UndoCommand(gameService).execute();
            case 7 -> {
                if (isWeekend) {
                    new WeekendSummaryCommand(gameService).execute();
                } else {
                    new ExitCommand().execute();
                }
            }
            case 8 -> {
                if (isWeekend) {
                    new ExitCommand().execute();
                } else {
                    System.out.println("Invalid choice. Please try again.");
                }
            }
            default -> System.out.println("Invalid choice. Please try again.");
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
