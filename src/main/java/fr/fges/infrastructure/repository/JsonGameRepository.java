package fr.fges.infrastructure.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.fges.domain.model.BoardGame;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * JsonGameRepository - Implémentation JSON du repository
 */
public class JsonGameRepository implements GameRepository {
    private final String filePath;
    private final ObjectMapper mapper;

    public JsonGameRepository(String filePath) {
        this.filePath = filePath;
        this.mapper = new ObjectMapper();
    }

    @Override
    public List<BoardGame> findAll() {
        File file = new File(filePath);

        if (!file.exists()) {
            return new ArrayList<>();
        }

        try {
            // On désérialise le JSON en liste de BoardGame
            return mapper.readValue(file, new TypeReference<List<BoardGame>>() {
            });
        } catch (IOException e) {
            System.err.println("Error loading from JSON: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public void save(List<BoardGame> games) {
        try {
            // On sérialise la liste en JSON avec indentation
            mapper.writerWithDefaultPrettyPrinter()
                    .writeValue(new File(filePath), games);
        } catch (IOException e) {
            System.err.println("Error saving to JSON: " + e.getMessage());
        }
    }
}
