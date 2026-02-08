package fr.fges.domain.model;

import java.util.Stack;

/**
 * ActionHistory - Gestionnaire de l'historique des actions avec une Stack (pile)
 * 
 * Cette classe gère toutes les actions effectuées depuis le démarrage du programme.
 * Elle utilise une Stack (pile LIFO = Last In First Out) pour stocker les actions.
 * 
 * Principe LIFO :
 * - La dernière action ajoutée (push) sera la première retirée (pop)
 * - Exemple : Si on ajoute A, puis B, puis C
 *   Le prochain pop() retournera C, puis B, puis A
 * 
 * Exemple de scénario :
 * 1. User ajoute "Catan" -> history.push(AddAction(Catan))
 * 2. User ajoute "7 Wonders" -> history.push(AddAction(7 Wonders))
 * 3. Stack : [AddAction(Catan), AddAction(7 Wonders)] <- TOP
 * 4. User fait Undo -> history.pop() retourne AddAction(7 Wonders)
 * 5. Stack : [AddAction(Catan)] <- TOP
 * 6. User fait Undo -> history.pop() retourne AddAction(Catan)
 * 7. Stack : [] (vide)
 */
public class ActionHistory {
    // Stack (pile) qui stocke toutes les actions dans l'ordre chronologique
    // Le sommet de la pile (top) = la dernière action effectuée
    private final Stack<Action> history;

    /**
     * Constructeur - Initialise une pile vide
     * Cette pile va grandir au fur et à mesure des actions Add/Remove
     */
    public ActionHistory() {
        this.history = new Stack<>();
    }

    /**
     * Ajoute une action au sommet de la pile (PUSH)
     * 
     * Appelé par GameService.addGame() et GameService.removeGame()
     * après chaque action d'ajout ou de suppression
     * 
     * @param action L'action à ajouter (AddAction ou RemoveAction)
     */
    public void addAction(Action action) {
        // Push = empiler au sommet de la pile
        history.push(action);
    }

    /**
     * Annule la dernière action en la retirant de la pile (POP)
     * 
     * Processus :
     * 1. Vérifie si la pile n'est pas vide
     * 2. Retire (pop) la dernière action du sommet
     * 3. Appelle action.undo() pour l'annuler
     * 
     * @return true si une action a été annulée, false si la pile était vide
     */
    public boolean undoLastAction() {
        // Vérifie s'il y a des actions à annuler
        if (history.isEmpty()) {
            return false; // Rien à annuler
        }
        
        // Pop = retirer et récupérer l'action au sommet de la pile
        Action action = history.pop();
        
        // Appelle la méthode undo() de l'action
        // Si c'est AddAction -> supprime le jeu
        // Si c'est RemoveAction -> ré-ajoute le jeu
        action.undo();
        
        return true; // Action annulée avec succès
    }

    /**
     * Vérifie s'il y a des actions disponibles pour l'annulation
     * 
     * Utilisé par UndoCommand pour afficher "Nothing to undo." si la pile est vide
     * 
     * @return true s'il y a au moins une action, false si la pile est vide
     */
    public boolean hasActions() {
        return !history.isEmpty();
    }

    /**
     * Consulte la dernière action SANS la retirer de la pile (PEEK)
     * 
     * Différence avec pop() :
     * - pop() : retire ET retourne l'action (destructif)
     * - peek() : retourne l'action SANS la retirer (non-destructif)
     * 
     * Utilisé pour récupérer le message d'annulation avant d'annuler l'action
     * 
     * @return La dernière action ou null si la pile est vide
     */
    public Action peekLastAction() {
        if (history.isEmpty()) {
            return null; // Pas d'action disponible
        }
        // Peek = regarder au sommet sans retirer
        return history.peek();
    }
}
