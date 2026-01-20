package fr.fges;

public class Main {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java -jar boardgamecollection.jar <storage-file>");
            System.out.println("Storage file must be .json or .csv");
            System.exit(1);
        }

        String storageFile = args[0];

        // Check file extension
        if (!storageFile.endsWith(".json") && !storageFile.endsWith(".csv")) {
            System.out.println("Error: Storage file must have .json or .csv extension");
            System.exit(1);
        }

        GameCollection.setStorageFile(storageFile);
        GameCollection.loadFromFile();

        System.out.println("Using storage file: " + storageFile);

        while (true) {
            Menu.handleMenu();
        }
    }
}
