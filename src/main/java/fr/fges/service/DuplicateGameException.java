package fr.fges.service;

/**
 * Exception levée lorsqu'on tente d'ajouter un jeu avec un titre qui existe déjà
 * dans la collection.
 * Cette exception hérite de RuntimeException pour ne pas obliger le catch partout
 * mais permet quand même une gestion fine des erreurs métier.
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
