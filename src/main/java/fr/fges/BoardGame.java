package fr.fges;

public record BoardGame(
        String title,
        int minPlayers,
        int maxPlayers,
        String category
) {
}
