package fr.fges.application.command;

import fr.fges.exceptions.MenuExitException;

/**
 * ExitCommand - Commande pour quitter l'application
 */
public class ExitCommand implements Command {

    @Override
    public void execute() {
        System.out.println("Exiting the application. Goodbye!");

    }
}
