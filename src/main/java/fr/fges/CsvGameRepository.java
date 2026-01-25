package fr.fges;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CsvGameRepository implements GameRepository {
    private final String filePath;

    public CsvGameRepository(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public List<BoardGame> findAll() {
        List<BoardGame> games = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists()) {
            return games;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
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
                            parts[3]);
                    games.add(game);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading from CSV: " + e.getMessage());
        }
        return games;
    }

    @Override
    public void save(List<BoardGame> games) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("title,minPlayers,maxPlayers,category");
            writer.newLine();
            for (BoardGame game : games) {
                writer.write(game.title() + "," + game.minPlayers() + "," + game.maxPlayers() + "," + game.category());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving to CSV: " + e.getMessage());
        }
    }
}
