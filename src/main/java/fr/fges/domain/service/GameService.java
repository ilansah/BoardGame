package fr.fges.domain.service;

import fr.fges.domain.model.Action;
import fr.fges.domain.model.ActionHistory;
import fr.fges.domain.model.AddAction;
import fr.fges.domain.model.BoardGame;
import fr.fges.domain.model.RemoveAction;
import fr.fges.exceptions.DuplicateGameException;
import fr.fges.infrastructure.repository.GameRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * GameService - Service métier pour la gestion des jeux de société
 * 
 * Gestion de l'historique Undo :
 * - Chaque action Add/Remove est enregistrée dans ActionHistory
 * - L'historique persiste pendant toute l'exécution du programme
 * - Les méthodes "Directly" permettent de modifier sans créer d'historique (pour le undo)
 */
public class GameService {
    private final List<BoardGame> games;
    private final GameRepository repository;
    
    // UNDO : Historique de toutes les actions Add/Remove depuis le démarrage
    // Initialisé dans le constructeur et persiste pendant toute l'exécution
    private final ActionHistory actionHistory;

    public GameService(GameRepository repository) {
        this.repository = repository;
        this.games = new ArrayList<>();
        
        // UNDO : Crée l'historique vide au démarrage
        // Cet historique va grandir au fur et à mesure des actions
        this.actionHistory = new ActionHistory();
        
        loadGames();
    }

    private void loadGames() {
        List<BoardGame> loadedGames = repository.findAll();
        games.clear();
        games.addAll(loadedGames);
    }

    /**
     * Ajoute un jeu à la collection
     * 
     * UNDO : Cette méthode enregistre automatiquement l'action dans l'historique
     * 
     * Flux d'exécution :
     * 1. Vérifie que le jeu n'existe pas déjà
     * 2. Ajoute le jeu à la collection
     * 3. Sauvegarde dans le fichier
     * 4. Crée une AddAction et l'empile dans l'historique
     * 
     * @param game Le jeu à ajouter
     * @throws DuplicateGameException Si un jeu avec ce titre existe déjà
     */
    public void addGame(BoardGame game) {
        if (existsByTitle(game.title())) {
            throw new DuplicateGameException(game.title());
        }
        
        // Ajoute le jeu à la collection en mémoire
        games.add(game);
        
        // Sauvegarde dans le fichier (CSV ou JSON)
        saveGames();
        
        // UNDO : Enregistre cette action pour pouvoir l'annuler plus tard
        // Crée une AddAction qui sait comment annuler cet ajout (en supprimant le jeu)
        actionHistory.addAction(new AddAction(game, this));
    }

    /**
     * Supprime un jeu de la collection
     * 
     * UNDO : Cette méthode enregistre automatiquement l'action dans l'historique
     * 
     * Flux d'exécution :
     * 1. Cherche le jeu par son titre
     * 2. Si trouvé, capture l'objet BoardGame COMPLET (crucial pour le undo !)
     * 3. Supprime le jeu de la collection
     * 4. Sauvegarde dans le fichier
     * 5. Crée une RemoveAction avec le jeu capturé et l'empile dans l'historique
     * 
     * @param title Le titre du jeu à supprimer
     */
    public void removeGame(String title) {
        // Cherche le jeu avant de le supprimer
        Optional<BoardGame> gameToRemove = findByTitle(title);
        
        if (gameToRemove.isPresent()) {
            // Récupère l'objet BoardGame complet
            BoardGame game = gameToRemove.get();
            
            // Supprime le jeu de la collection
            games.removeIf(g -> g.title().equals(title));
            
            // Sauvegarde dans le fichier
            saveGames();
            
            // UNDO : Enregistre cette action pour pouvoir l'annuler plus tard
            // IMPORTANT : On passe 'game' (l'objet complet) pour pouvoir le restaurer
            // Si on ne capturait que le titre, on perdrait les infos (min/max players, category)
            actionHistory.addAction(new RemoveAction(game, this));
        }
    }

    /**
     * UNDO : Ajoute un jeu directement SANS enregistrer dans l'historique
     * 
     * Cette méthode est utilisée UNIQUEMENT par RemoveAction.undo()
     * pour ré-ajouter un jeu lors de l'annulation d'une suppression.
     * 
     * Pourquoi une méthode séparée ?
     * Si on utilisait addGame(), ça créerait une nouvelle AddAction dans l'historique
     * -> On aurait une boucle infinie d'actions !
     * 
     * Exemple de scénario sans cette méthode :
     * 1. User supprime "Catan" -> RemoveAction dans stack
     * 2. User fait Undo -> RemoveAction.undo() appelle addGame()
     * 3. addGame() crée une AddAction dans stack
     * 4. User fait Undo -> AddAction.undo() appelle removeGame()
     * 5. removeGame() crée une RemoveAction dans stack
     * 6. BOUCLE INFINIE !
     * 
     * @param game Le jeu à ajouter
     */
    public void addGameDirectly(BoardGame game) {
        // Ajoute le jeu à la collection
        games.add(game);
        
        // Sauvegarde dans le fichier (les modifications doivent être persistées)
        saveGames();
        
        // AUCUNE action ajoutée à l'historique !
    }

    /**
     * UNDO : Supprime un jeu directement SANS enregistrer dans l'historique
     * 
     * Cette méthode est utilisée UNIQUEMENT par AddAction.undo()
     * pour supprimer un jeu lors de l'annulation d'un ajout.
     * 
     * Pourquoi une méthode séparée ?
     * Si on utilisait removeGame(), ça créerait une nouvelle RemoveAction dans l'historique
     * -> On aurait une boucle infinie d'actions !
     * 
     * Voir addGameDirectly() pour l'explication complète du problème de boucle infinie
     * 
     * @param title Le titre du jeu à supprimer
     */
    public void removeGameDirectly(String title) {
        // Supprime le jeu de la collection
        games.removeIf(game -> game.title().equals(title));
        
        // Sauvegarde dans le fichier (les modifications doivent être persistées)
        saveGames();
        
        // AUCUNE action ajoutée à l'historique !
    }

    public List<BoardGame> getAllGames() {
        return new ArrayList<>(games);
    }

    public List<BoardGame> getSortedGames() {
        return games.stream()
                .sorted(Comparator.comparing(BoardGame::title))
                .toList();
    }

    public Optional<BoardGame> findByTitle(String title) {
        return games.stream()
                .filter(game -> game.title().equals(title))
                .findFirst();
    }

    public boolean isEmpty() {
        return games.isEmpty();
    }

    public boolean existsByTitle(String title) {
        return games.stream()
                .anyMatch(game -> game.title().equalsIgnoreCase(title));
    }

    public BoardGame recommendGame(int playerCount) {

        List<BoardGame> compatibleGames = games.stream()
                .filter(game -> game.minPlayers() <= playerCount && game.maxPlayers() >= playerCount)
                .toList();

        if (compatibleGames.isEmpty()) {
            return null;
        }

        int randomIndex = (int) (Math.random() * compatibleGames.size());
        return compatibleGames.get(randomIndex);
    }

    /**
     * UNDO : Annule la dernière action effectuée
     * 
     * Délègue à ActionHistory qui va :
     * 1. Pop (retirer) la dernière action de la pile
     * 2. Appeler action.undo() pour l'annuler
     * 
     * Si c'était une AddAction -> le jeu sera supprimé
     * Si c'était une RemoveAction -> le jeu sera ré-ajouté
     * 
     * @return true si une action a été annulée, false si l'historique est vide
     */
    public boolean undo() {
        return actionHistory.undoLastAction();
    }

    /**
     * UNDO : Vérifie s'il y a des actions disponibles pour l'annulation
     * 
     * Utilisé par UndoCommand pour détecter si la pile est vide
     * avant de tenter une annulation.
     * 
     * @return true s'il y a au moins une action, false si l'historique est vide
     */
    public boolean hasActionsToUndo() {
        return actionHistory.hasActions();
    }

    /**
     * UNDO : Récupère la dernière action SANS l'annuler
     * 
     * Utilisé par UndoCommand pour récupérer le message d'annulation
     * AVANT d'annuler l'action (car après elle n'est plus dans la pile).
     * 
     * Utilise peek() au lieu de pop() pour ne pas retirer l'action
     * 
     * @return La dernière action ou null si l'historique est vide
     */
    public Action getLastAction() {
        return actionHistory.peekLastAction();
    }

    public List<BoardGame> getRandomGames(int count) {
        List<BoardGame> result = new ArrayList<>();
        List<BoardGame> availableGames = new ArrayList<>(games);

        int actualCount = Math.min(count, availableGames.size());

        for (int i = 0; i < actualCount; i++) {
            int randomIndex = (int) (Math.random() * availableGames.size());

            result.add(availableGames.remove(randomIndex));
        }

        return result;
    }

    public List<BoardGame> findGamesByPlayerCount(int playerCount) {
        return games.stream()
                .filter(game -> game.minPlayers() <= playerCount && game.maxPlayers() >= playerCount)
                .sorted(Comparator.comparing(BoardGame::title))
                .toList();
    }

    private void saveGames() {
        repository.save(games);
    }
}
