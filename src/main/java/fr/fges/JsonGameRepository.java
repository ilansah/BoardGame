package fr.fges;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(filePath), games);
        } catch (IOException e) {
            System.err.println("Error saving to JSON: " + e.getMessage());
        }
    }
}
