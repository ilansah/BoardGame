package fr.fges.domain.model;

/**
 * Action - Interface pour les actions annulables (pattern Command)
 * 
 * Cette interface définit le contrat que toutes les actions annulables doivent respecter.
 * C'est la base du système Undo : chaque action (Add, Remove) implémente cette interface.
 * 
 * Principe : Une action sait comment s'annuler elle-même (undo)
 */
public interface Action {
    
    /**
     * Annule l'action effectuée
     * 
     * Pour AddAction : annuler un ajout = supprimer le jeu
     * Pour RemoveAction : annuler une suppression = ré-ajouter le jeu
     */
    void undo();

    /**
     * Retourne une description de l'action effectuée
     * Ex: "Add \"Catan\"" ou "Remove \"7 Wonders\""
     * 
     * Utilisé pour le logging ou debug
     */
    String getDescription();

    /**
     * Retourne le message à afficher lors de l'annulation
     * Ex: "Removed \"Catan\" from collection." (quand on annule un Add)
     * Ex: "Re-added \"7 Wonders\" to collection." (quand on annule un Remove)
     * 
     * Ce message décrit CE QUI A ÉTÉ FAIT pour annuler, pas l'action originale
     */
    String getUndoMessage();
}
