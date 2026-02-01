package fr.fges.service;

import fr.fges.BoardGame;
import fr.fges.GameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.ArgumentCaptor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests unitaires pour la classe GameService
 * Utilise Mockito pour mocker le GameRepository
 */
@DisplayName("GameService Tests")
class GameServiceTest {

    private GameRepository mockRepository;
    private GameService gameService;
    private List<BoardGame> testGames;

    @BeforeEach
    void setUp() {
        // Créer un mock du repository
        mockRepository = mock(GameRepository.class);

        // Créer des jeux de test
        testGames = new ArrayList<>();
        testGames.add(new BoardGame("Catan", 3, 4, "strategy"));
        testGames.add(new BoardGame("Azul", 2, 4, "family"));
        testGames.add(new BoardGame("7 Wonders", 2, 7, "strategy"));

        // Configurer le mock pour retourner les jeux de test lors du chargement
        when(mockRepository.findAll()).thenReturn(new ArrayList<>(testGames));

        // Créer le service (qui va charger les jeux depuis le mock)
        gameService = new GameService(mockRepository);
    }

    @Test
    @DisplayName("Le constructeur doit charger les jeux depuis le repository")
    void constructor_shouldLoadGamesFromRepository() {
        // Vérifier que findAll() a été appelé lors de la construction
        verify(mockRepository, times(1)).findAll();

        // Vérifier que les jeux ont été chargés
        assertEquals(3, gameService.getAllGames().size());
    }

    @Test
    @DisplayName("addGame doit ajouter un jeu et sauvegarder")
    void addGame_shouldAddGameAndSave() {
        // Créer un nouveau jeu
        BoardGame newGame = new BoardGame("Pandemic", 2, 4, "cooperative");

        // Ajouter le jeu
        gameService.addGame(newGame);

        // Vérifier que le jeu a été ajouté
        assertEquals(4, gameService.getAllGames().size());
        assertTrue(gameService.getAllGames().contains(newGame));

        // Vérifier que save() a été appelé
        verify(mockRepository, times(1)).save(anyList());
    }

    @Test
    @DisplayName("removeGame doit supprimer un jeu par son titre et sauvegarder")
    void removeGame_shouldRemoveGameByTitleAndSave() {
        // Supprimer un jeu existant
        gameService.removeGame("Catan");

        // Vérifier que le jeu a été supprimé
        assertEquals(2, gameService.getAllGames().size());
        assertFalse(gameService.getAllGames().stream()
                .anyMatch(game -> game.title().equals("Catan")));

        // Vérifier que save() a été appelé
        verify(mockRepository, times(1)).save(anyList());
    }

    @Test
    @DisplayName("removeGame ne doit rien faire si le jeu n'existe pas")
    void removeGame_shouldDoNothingIfGameDoesNotExist() {
        // Supprimer un jeu qui n'existe pas
        gameService.removeGame("Jeu Inexistant");

        // Vérifier que la taille n'a pas changé
        assertEquals(3, gameService.getAllGames().size());

        // Vérifier que save() a quand même été appelé
        verify(mockRepository, times(1)).save(anyList());
    }

    @Test
    @DisplayName("getAllGames doit retourner une copie défensive de la liste")
    void getAllGames_shouldReturnDefensiveCopy() {
        // Récupérer la liste
        List<BoardGame> games = gameService.getAllGames();

        // Modifier la liste retournée
        games.clear();

        // Vérifier que la liste interne n'a pas été modifiée
        assertEquals(3, gameService.getAllGames().size());
    }

    @Test
    @DisplayName("getSortedGames doit retourner les jeux triés par titre")
    void getSortedGames_shouldReturnGamesSortedByTitle() {
        // Récupérer les jeux triés
        List<BoardGame> sortedGames = gameService.getSortedGames();

        // Vérifier l'ordre alphabétique
        assertEquals("7 Wonders", sortedGames.get(0).title());
        assertEquals("Azul", sortedGames.get(1).title());
        assertEquals("Catan", sortedGames.get(2).title());
    }

    @Test
    @DisplayName("findByTitle doit retourner le jeu correspondant")
    void findByTitle_shouldReturnMatchingGame() {
        // Rechercher un jeu existant
        Optional<BoardGame> result = gameService.findByTitle("Azul");

        // Vérifier que le jeu a été trouvé
        assertTrue(result.isPresent());
        assertEquals("Azul", result.get().title());
        assertEquals(2, result.get().minPlayers());
        assertEquals(4, result.get().maxPlayers());
        assertEquals("family", result.get().category());
    }

    @Test
    @DisplayName("findByTitle doit retourner Optional.empty si le jeu n'existe pas")
    void findByTitle_shouldReturnEmptyIfGameDoesNotExist() {
        // Rechercher un jeu qui n'existe pas
        Optional<BoardGame> result = gameService.findByTitle("Jeu Inexistant");

        // Vérifier que le résultat est vide
        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("isEmpty doit retourner false quand il y a des jeux")
    void isEmpty_shouldReturnFalseWhenGamesExist() {
        assertFalse(gameService.isEmpty());
    }

    @Test
    @DisplayName("isEmpty doit retourner true quand il n'y a pas de jeux")
    void isEmpty_shouldReturnTrueWhenNoGames() {
        // Créer un service avec un repository vide
        when(mockRepository.findAll()).thenReturn(new ArrayList<>());
        GameService emptyService = new GameService(mockRepository);

        assertTrue(emptyService.isEmpty());
    }

    @Test
    @DisplayName("save doit être appelé avec la liste correcte lors de l'ajout")
    void save_shouldBeCalledWithCorrectListOnAdd() {
        // Créer un captor pour capturer l'argument passé à save()
        ArgumentCaptor<List<BoardGame>> captor = ArgumentCaptor.forClass(List.class);

        // Ajouter un jeu
        BoardGame newGame = new BoardGame("Risk", 2, 6, "strategy");
        gameService.addGame(newGame);

        // Capturer l'argument passé à save()
        verify(mockRepository).save(captor.capture());

        // Vérifier que la liste contient le nouveau jeu
        List<BoardGame> savedGames = captor.getValue();
        assertEquals(4, savedGames.size());
        assertTrue(savedGames.contains(newGame));
    }

    @Test
    @DisplayName("Plusieurs suppressions successives doivent fonctionner correctement")
    void multipleRemoves_shouldWorkCorrectly() {
        // Supprimer plusieurs jeux
        gameService.removeGame("Catan");
        gameService.removeGame("Azul");

        // Vérifier qu'il ne reste qu'un jeu
        assertEquals(1, gameService.getAllGames().size());
        assertEquals("7 Wonders", gameService.getAllGames().get(0).title());

        // Vérifier que save() a été appelé 2 fois
        verify(mockRepository, times(2)).save(anyList());
    }

    @Test
    @DisplayName("Plusieurs ajouts successifs doivent fonctionner correctement")
    void multipleAdds_shouldWorkCorrectly() {
        // Ajouter plusieurs jeux
        gameService.addGame(new BoardGame("Monopoly", 2, 8, "family"));
        gameService.addGame(new BoardGame("Chess", 2, 2, "strategy"));

        // Vérifier que les jeux ont été ajoutés
        assertEquals(5, gameService.getAllGames().size());

        // Vérifier que save() a été appelé 2 fois
        verify(mockRepository, times(2)).save(anyList());
    }

    @Test
    @DisplayName("L'ajout d'un jeu avec un titre existant doit lever une DuplicateGameException")
    void addGame_withDuplicateTitle_shouldThrowException() {
        // Tenter d'ajouter un jeu avec un titre qui existe déjà
        BoardGame duplicate = new BoardGame("Catan", 2, 4, "strategy");

        // Vérifier que l'exception est levée
        DuplicateGameException exception = assertThrows(
                DuplicateGameException.class,
                () -> gameService.addGame(duplicate)
        );

        // Vérifier le message et le titre
        assertEquals("Catan", exception.getGameTitle());
        assertTrue(exception.getMessage().contains("Catan"));

        // Vérifier que le jeu n'a pas été ajouté
        assertEquals(3, gameService.getAllGames().size());

        // Vérifier que save() n'a jamais été appelé (pas d'ajout)
        verify(mockRepository, never()).save(anyList());
    }

    @Test
    @DisplayName("L'ajout d'un jeu avec un titre existant (différente casse) doit lever une exception")
    void addGame_withDuplicateTitleDifferentCase_shouldThrowException() {
        // Tenter d'ajouter un jeu avec le même titre mais en majuscules
        BoardGame duplicate = new BoardGame("CATAN", 2, 4, "strategy");

        // Vérifier que l'exception est levée (case insensitive)
        assertThrows(DuplicateGameException.class, () -> gameService.addGame(duplicate));

        // Vérifier que le jeu n'a pas été ajouté
        assertEquals(3, gameService.getAllGames().size());
    }

    @Test
    @DisplayName("existsByTitle doit retourner true pour un titre existant")
    void existsByTitle_withExistingTitle_shouldReturnTrue() {
        assertTrue(gameService.existsByTitle("Catan"));
        assertTrue(gameService.existsByTitle("catan")); // case insensitive
        assertTrue(gameService.existsByTitle("CATAN"));
    }

    @Test
    @DisplayName("existsByTitle doit retourner false pour un titre inexistant")
    void existsByTitle_withNonExistingTitle_shouldReturnFalse() {
        assertFalse(gameService.existsByTitle("Monopoly"));
        assertFalse(gameService.existsByTitle(""));
    }
