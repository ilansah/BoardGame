package fr.fges;

public class Main {
    public static void main(String[] args) {
        // Utilise games.json par défaut si aucun argument n'est fourni
        String storageFile;
        if (args.length < 1) {
            storageFile = "games.json";
            System.out.println("Aucun fichier spécifié, utilisation du fichier par défaut: " + storageFile);
        } else {
            storageFile = args[0];
        }

        // Check file extension
        if (!storageFile.endsWith(".json") && !storageFile.endsWith(".csv")) {
            System.out.println("Error: Storage file must have .json or .csv extension");
            // System.exit(1);
            return;
        }

        GameCollection gameCollection = new GameCollection();
        gameCollection.setStorageFile(storageFile);
        gameCollection.loadFromFile();

        System.out.println("Using storage file: " + storageFile);

        Menu menu = new Menu(gameCollection);
        while (true) {
            try {
                menu.handleMenu();
            } catch (MenuExitException e) {
                break;
            }
        }
    }
}
