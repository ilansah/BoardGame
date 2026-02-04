package fr.fges.presentation;

/**
 * WeekdayMenuDisplay - Affichage du menu en semaine
 */
public class WeekdayMenuDisplay implements MenuDisplay {

    @Override
    public void display() {
        String menuText = """
                === Board Game Collection ===
                1. Add Board Game
                2. Remove Board Game
                3. List All Board Games
                4. Recommend Game
                5. Exit
                Please select an option (1-5):
                """;
        System.out.print(menuText);
    }

    @Override
    public int getMaxOption() {
        return 5;
    }
}
