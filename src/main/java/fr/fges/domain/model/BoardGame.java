package fr.fges.domain.model;

/**
 * BoardGame - Entité métier représentant un jeu de société
 */
public record BoardGame(
                String title,
                int minPlayers,
                int maxPlayers,
                String category) {
}
