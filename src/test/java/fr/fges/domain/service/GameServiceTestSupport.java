package fr.fges.domain.service;

import fr.fges.domain.model.BoardGame;
import fr.fges.domain.port.GameRepository;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Support commun pour les tests des services de jeux.
 */
public abstract class GameServiceTestSupport {
    protected GameRepository mockRepository;
    protected List<BoardGame> testGames;

    protected void initRepositoryWithDefaultGames() {
        initRepositoryWithGames(List.of(
                new BoardGame("Catan", 3, 4, "strategy"),
                new BoardGame("Azul", 2, 4, "family"),
                new BoardGame("7 Wonders", 2, 7, "strategy")
        ));
    }

    protected void initRepositoryWithGames(List<BoardGame> games) {
        mockRepository = mock(GameRepository.class);
        testGames = new ArrayList<>(games);

        when(mockRepository.findAll()).thenAnswer(invocation -> new ArrayList<>(testGames));
        doAnswer(invocation -> {
            List<BoardGame> savedGames = invocation.getArgument(0);
            testGames = new ArrayList<>(savedGames);
            return null;
        }).when(mockRepository).save(anyList());
    }
}