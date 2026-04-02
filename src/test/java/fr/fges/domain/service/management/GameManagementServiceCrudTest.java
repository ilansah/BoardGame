package fr.fges.domain.service.management;

import fr.fges.domain.model.BoardGame;
import fr.fges.domain.service.GameManagementService;
import fr.fges.domain.service.GameServiceTestSupport;
import fr.fges.exceptions.DuplicateGameException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@DisplayName("GameManagementService CRUD Tests")
class GameManagementServiceCrudTest extends GameServiceTestSupport {
    private GameManagementService gameManagementService;

    @BeforeEach
    void setUp() {
        initRepositoryWithDefaultGames();
        gameManagementService = new GameManagementService(mockRepository);
    }

    @Test
    @DisplayName("Le constructeur doit charger les jeux depuis le repository")
    void constructor_shouldLoadGamesFromRepository() {
        verify(mockRepository, times(1)).findAll();
        assertEquals(3, gameManagementService.getAllGames().size());
    }

    @Test
    @DisplayName("addGame doit ajouter un jeu et sauvegarder")
    void addGame_shouldAddGameAndSave() {
        BoardGame newGame = new BoardGame("Pandemic", 2, 4, "cooperative");

        gameManagementService.addGame(newGame);

        assertEquals(4, gameManagementService.getAllGames().size());
        assertTrue(gameManagementService.getAllGames().contains(newGame));
        verify(mockRepository, times(1)).save(anyList());
    }

    @Test
    @DisplayName("removeGame doit supprimer un jeu par son titre et sauvegarder")
    void removeGame_shouldRemoveGameByTitleAndSave() {
        gameManagementService.removeGame("Catan");

        assertEquals(2, gameManagementService.getAllGames().size());
        assertTrue(gameManagementService.getAllGames().stream().noneMatch(game -> game.title().equals("Catan")));
        verify(mockRepository, times(1)).save(anyList());
    }

    @Test
    @DisplayName("removeGame ne doit rien faire si le jeu n'existe pas")
    void removeGame_shouldDoNothingIfGameDoesNotExist() {
        gameManagementService.removeGame("Jeu Inexistant");

        assertEquals(3, gameManagementService.getAllGames().size());
        verify(mockRepository, never()).save(anyList());
    }

    @Test
    @DisplayName("save doit être appelé avec la liste correcte lors de l'ajout")
    void save_shouldBeCalledWithCorrectListOnAdd() {
        ArgumentCaptor<List<BoardGame>> captor = ArgumentCaptor.forClass(List.class);

        BoardGame newGame = new BoardGame("Risk", 2, 6, "strategy");
        gameManagementService.addGame(newGame);

        verify(mockRepository).save(captor.capture());

        List<BoardGame> savedGames = captor.getValue();
        assertEquals(4, savedGames.size());
        assertTrue(savedGames.contains(newGame));
    }

    @Test
    @DisplayName("Plusieurs suppressions successives doivent fonctionner correctement")
    void multipleRemoves_shouldWorkCorrectly() {
        gameManagementService.removeGame("Catan");
        gameManagementService.removeGame("Azul");

        assertEquals(1, gameManagementService.getAllGames().size());
        assertEquals("7 Wonders", gameManagementService.getAllGames().get(0).title());
        verify(mockRepository, times(2)).save(anyList());
    }

    @Test
    @DisplayName("Plusieurs ajouts successifs doivent fonctionner correctement")
    void multipleAdds_shouldWorkCorrectly() {
        gameManagementService.addGame(new BoardGame("Monopoly", 2, 8, "family"));
        gameManagementService.addGame(new BoardGame("Chess", 2, 2, "strategy"));

        assertEquals(5, gameManagementService.getAllGames().size());
        verify(mockRepository, times(2)).save(anyList());
    }

    @Test
    @DisplayName("L'ajout d'un jeu avec un titre existant doit lever une DuplicateGameException")
    void addGame_withDuplicateTitle_shouldThrowException() {
        BoardGame duplicate = new BoardGame("Catan", 2, 4, "strategy");

        DuplicateGameException exception = assertThrows(
                DuplicateGameException.class,
                () -> gameManagementService.addGame(duplicate));

        assertEquals("Catan", exception.getGameTitle());
        assertTrue(exception.getMessage().contains("Catan"));
        assertEquals(3, gameManagementService.getAllGames().size());
        verify(mockRepository, never()).save(anyList());
    }

    @Test
    @DisplayName("L'ajout d'un jeu avec un titre existant (différente casse) doit lever une exception")
    void addGame_withDuplicateTitleDifferentCase_shouldThrowException() {
        BoardGame duplicate = new BoardGame("CATAN", 2, 4, "strategy");

        assertThrows(DuplicateGameException.class, () -> gameManagementService.addGame(duplicate));
        assertEquals(3, gameManagementService.getAllGames().size());
    }
}