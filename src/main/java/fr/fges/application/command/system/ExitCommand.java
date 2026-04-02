package fr.fges.application.command.system;

import fr.fges.application.command.Command;
import fr.fges.exceptions.MenuExitException;

/**
 * ExitCommand - Commande pour quitter l'application
 */
public class ExitCommand implements Command {

    @Override
    public void execute() {
        System.out.println("Exiting the application. Goodbye!");
        throw new MenuExitException();
    }

    @Override
    public String getName() {
        return "Exit";
    }
}