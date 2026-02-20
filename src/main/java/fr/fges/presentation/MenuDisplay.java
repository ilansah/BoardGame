package fr.fges.presentation;

/**
 * MenuDisplay - Classe simple pour l'affichage du menu
 */
public class MenuDisplay {

    /**
     * Affiche le menu en fonction du jour (weekend ou semaine)
     * @param isWeekend true si c'est le weekend, false sinon
     */
    public void display(boolean isWeekend) {
        System.out.println("\n=== Board Game Collection ===");
        System.out.println("1. Add Board Game");
        System.out.println("2. Remove Board Game");
        System.out.println("3. List All Board Games");
        System.out.println("4. Recommend Game");
        System.out.println("5. Find Games by Player Count");
        System.out.println("6. Undo Last Action");
        
        if (isWeekend) {
            System.out.println("7. Weekend Summary");
            System.out.println("8. Exit");
            System.out.print("Please select an option (1-8): ");
        } else {
            System.out.println("7. Exit");
            System.out.print("Please select an option (1-7): ");
        }
    }
}
