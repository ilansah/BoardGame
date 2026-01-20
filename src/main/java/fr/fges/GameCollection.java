package fr.fges;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GameCollection {
    private static final List<BoardGame> games = new ArrayList<>();
    private static String storageFile = "";

    public static void setStorageFile(String file) {
        storageFile = file;
    }

    public static List<BoardGame> getGames() {
        return games;
    }

    public static void addGame(BoardGame game) {
        games.add(game);
        saveToFile();
    }

    public static void removeGame(BoardGame game) {
        games.remove(game);
        saveToFile();
    }

    public static void viewAllGames() {
        if (games.isEmpty()) {
            System.out.println("No board games in collection.");
            return;
        }

        // Sort the games by their title alphabetically
        List<BoardGame> sortedGames = games.stream()
                .sorted(Comparator.comparing(BoardGame::title))
                .toList();

        for (BoardGame game : sortedGames) {
            System.out.println("Game: " + game.title() + " (" + game.minPlayers() + "-" + game.maxPlayers() + " players) - " + game.category());
        }
    }

    public static void loadFromFile() {
        File file = new File(storageFile);
        if (!file.exists()) {
            return;
        }

        if (storageFile.endsWith(".json")) {
            loadFromJson();
        } else if (storageFile.endsWith(".csv")) {
            loadFromCsv();
        }
    }

    private static void loadFromJson() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            File file = new File(storageFile);
            List<BoardGame> loadedGames = mapper.readValue(file, new TypeReference<List<BoardGame>>() {});
            games.clear();
            games.addAll(loadedGames);
        } catch (IOException e) {
            System.out.println("Error loading from JSON: " + e.getMessage());
        }
    }

    private static void loadFromCsv() {
        try (BufferedReader reader = new BufferedReader(new FileReader(storageFile))) {
            games.clear();
            String line;
            boolean firstLine = true;
            while ((line = reader.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue; // skip header
                }
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    BoardGame game = new BoardGame(
                            parts[0],
                            Integer.parseInt(parts[1]),
                            Integer.parseInt(parts[2]),
                            parts[3]
                    );
                    games.add(game);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading from CSV: " + e.getMessage());
        }
    }

    public static void saveToFile() {
        if (storageFile.endsWith(".json")) {
            saveToJson();
        } else if (storageFile.endsWith(".csv")) {
            saveToCsv();
        }
    }

    private static void saveToJson() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(storageFile), games);
        } catch (IOException e) {
            System.out.println("Error saving to JSON: " + e.getMessage());
        }
    }

    private static void saveToCsv() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(storageFile))) {
            writer.write("title,minPlayers,maxPlayers,category");
            writer.newLine();
            for (BoardGame game : games) {
                writer.write(game.title() + "," + game.minPlayers() + "," + game.maxPlayers() + "," + game.category());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving to CSV: " + e.getMessage());
        }
    }
}
