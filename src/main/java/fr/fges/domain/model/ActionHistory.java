package fr.fges.domain.model;

import java.util.Stack;

/**
 * ActionHistory - Gestionnaire de l'historique des actions avec une Stack (pile)
 */
public class ActionHistory {

    private final Stack<Action> history;

    public ActionHistory() {
        this.history = new Stack<>();
    }

    public void addAction(Action action) {
        history.push(action);
    }

    public boolean undoLastAction() {
        // Vérifie s'il y a des actions à annuler
        if (history.isEmpty()) {
            return false; // Rien à annuler
        }
        
        // Pop = retirer et récupérer l'action au sommet de la pile
        Action action = history.pop();

        action.undo();
        
        return true; // Action annulée avec succès
    }


    public boolean hasActions() {
        return !history.isEmpty();
    }


    public Action peekLastAction() {
        if (history.isEmpty()) {
            return null; // Pas d'action disponible
        }
        // Peek = regarder au sommet sans retirer
        return history.peek();
    }
}
