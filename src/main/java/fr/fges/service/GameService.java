package fr.fges.service;

import fr.fges.BoardGame;
import fr.fges.GameRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

// classe qui gere la logique metier de la collection
// elle fait que gerer les jeux pas l'affichage ni la sauvegarde directe
public class GameService {
    // la liste des jeux a ete deplacee ici depuis gamecollection
    private final List<BoardGame> games;

    // on depend d'une interface pas dune implementation concrete
    // comme ca on peut changer json csv xml sans toucher ce code
    private final GameRepository repository;

    // injection de dependance par le constructeur
    // on recoit le repository de lexterieur
    public GameService(GameRepository repository) {
        this.repository = repository;
        this.games = new ArrayList<>();
        loadGames(); // on charge direct au demarrage
    }

    // charge les jeux depuis le repository au demarrage
    private void loadGames() {
        List<BoardGame> loadedGames = repository.findAll();
        games.clear();
        games.addAll(loadedGames);
    }

    // ajoute un jeu et sauvegarde automatiquement
    // avant cetait dans gamecollection qui faisait tout
    public void addGame(BoardGame game) {
        games.add(game);
        saveGames(); // on delegue au repository
    }

    // supprime un jeu par son titre
    // avant on prenait lobjet complet maintenant juste le titre c'est plus simple
    public void removeGame(String title) {
        games.removeIf(game -> game.title().equals(title));
        saveGames();
    }

    // retourne tous les jeux sans tri
    // copie defensive pour proteger la liste interne
    public List<BoardGame> getAllGames() {
        return new ArrayList<>(games);
    }

    // logique de tri extraite de viewallgames
    // avant c'etait melange avec laffichage maintenant cest separe
    // le service fait le tri et le controllerfera l'affichage
    public List<BoardGame> getSortedGames() {
        return games.stream()
                .sorted(Comparator.comparing(BoardGame::title))
                .toList();
    }

    // recherche un jeu par titre
    // retourne optional pour eviter les null
    public Optional<BoardGame> findByTitle(String title) {
        return games.stream()
                .filter(game -> game.title().equals(title))
                .findFirst();
    }

    // verifie si la collection est vide
    public boolean isEmpty() {
        return games.isEmpty();
    }

    // sauvegarde via le repository
    // on sait pas comment ca sauvegarde json csv ou autre
    // c'est le repository qui gere ca
    private void saveGames() {
        repository.save(games);
    }
}
