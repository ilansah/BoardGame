package fr.fges.presentation;

import fr.fges.application.command.Command;
import java.util.List;

/**
 * MenuDisplay - Classe simple pour l'affichage du menu
 */
public class MenuDisplay {

    /**
     * Affiche le menu dynamiquement en fonction de la liste de commandes
     * @param commands liste de commandes à afficher
     */
    public void display(List<Command> commands) {
        System.out.println("\n=== Board Game Collection ===");
        
        for (int i = 0; i < commands.size(); i++) {
            System.out.println((i + 1) + ". " + commands.get(i).getName());
        }
        
        System.out.print("Please select an option (1-" + commands.size() + "): ");
    }
}
