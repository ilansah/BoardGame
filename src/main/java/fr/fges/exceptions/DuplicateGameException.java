package fr.fges.exceptions;

/**
 * DuplicateGameException - Exception levée lors d'un doublon
 */
public class DuplicateGameException extends RuntimeException {
    private final String gameTitle;

    public DuplicateGameException(String gameTitle) {
        super("Un jeu avec le titre \"" + gameTitle + "\" existe déjà dans la collection");
        this.gameTitle = gameTitle;
    }

    public String getGameTitle() {
        return gameTitle;
    }
}
