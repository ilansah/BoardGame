package fr.fges.presentation;

/**
 * WeekendMenuDisplay - Affichage du menu le weekend
 */
public class WeekendMenuDisplay implements MenuDisplay {

    @Override
    public void display() {
        String menuText = """
                === Board Game Collection ===
                1. Add Board Game
                2. Remove Board Game
                3. List All Board Games
                4. Recommend Game
                5. Find Games by Player Count
                6. Weekend Summary
                7. Exit
                Please select an option (1-7):
                """;
        System.out.print(menuText);
    }

    @Override
    public int getMaxOption() {
        return 7;
    }
}
