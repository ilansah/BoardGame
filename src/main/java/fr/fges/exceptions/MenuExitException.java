package fr.fges.exceptions;

/**
 * MenuExitException - Exception pour sortir proprement du menu
 */
public class MenuExitException extends RuntimeException {
    public MenuExitException() {
        super("Menu exited by user");
    }
}
