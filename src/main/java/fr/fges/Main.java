package fr.fges;

import fr.fges.domain.service.GameService;
import fr.fges.exceptions.MenuExitException;
import fr.fges.infrastructure.repository.CsvGameRepository;
import fr.fges.infrastructure.repository.GameRepository;
import fr.fges.infrastructure.repository.JsonGameRepository;
import fr.fges.presentation.Menu;

/**
 * Main - Point d'entrée de l'application
 */
public class Main {
    public static void main(String[] args) {
        String storageFile;
        if (args.length < 1) {
            storageFile = "games.json";
            System.out.println("Aucun fichier spécifié, utilisation du fichier par défaut: " + storageFile);
        } else {
            storageFile = args[0];
        }

        if (!storageFile.endsWith(".json") && !storageFile.endsWith(".csv")) {
            System.out.println("Error: Storage file must have .json or .csv extension");
            return;
        }

        GameRepository repository;
        if (storageFile.endsWith(".json")) {
            repository = new JsonGameRepository(storageFile);
        } else {
            repository = new CsvGameRepository(storageFile);
        }

        GameService gameService = new GameService(repository);

        System.out.println("Using storage file: " + storageFile);

        Menu menu = new Menu(gameService);

        while (true) {
            try {
                menu.handleMenu();
            } catch (MenuExitException e) {

                break;
            }
        }

        menu.close();
    }
}
