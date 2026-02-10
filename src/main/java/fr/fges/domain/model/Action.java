package fr.fges.domain.model;

/**
 * Action - Interface pour les actions annulables (pattern Command)
 */
public interface Action {
    
    void undo();

    String getDescription();
    
    String getUndoMessage();
}
