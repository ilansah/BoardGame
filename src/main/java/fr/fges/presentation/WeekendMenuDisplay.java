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
                6. Undo Last Action
                7. Weekend Summary
                8. Exit
                Please select an option (1-8):
                """;
        System.out.print(menuText);
    }

    @Override
    public int getMaxOption() {
        return 8;
    }
}
