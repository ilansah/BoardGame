# Rapport de Refactoring - Projet BoardGame

**Groupe :** Ilan, Antonin, Victorian
**Date :** 25/01/2025

## 1. Analyse de l'existant

En regardant le code actuel, on a vu que c'était assez mélangé. Les différentes parties du code dépendent trop les unes des autres :

- **GameCollection :** C'est la classe qui pose problème car elle fait tout toute seule. Elle gère la liste des jeux, la sauvegarde des fichiers et même l'affichage à l'écran.
- **BoardGame :** C'est juste le format des données, cette partie est très bien.
- **Menu :** Elle s'occupe de l'affichage, mais elle va chercher les informations directement dans `GameCollection` au lieu de passer par un intermédiaire.
- **Main :** Il sert juste à lancer le programme.

**Le vrai problème :** Si on veut changer la façon de sauvegarder ou l'affichage, on est obligé de modifier la classe `GameCollection` qui est déjà trop chargée.

## 2. Architecture proposée

Pour régler ça, on a décidé de découper le code en trois parties bien distinctes. Chacune aura un rôle précis :

1.  **Le Stockage (Repository) :** S'occupe uniquement de lire et écrire les fichiers de sauvegarde (CSV ou JSON).
2.  **La Logique (Service) :** S'occupe de gérer les jeux (ajouter, supprimer, trier la liste).
3.  **L'Interface (Controller/View) :** S'occupe de ce que l'utilisateur voit et de ce qu'il tape au clavier.

## 3. Ce qu'on a fait pour l'instant

Comme prévu, Ilan a commencé par s'occuper de la partie stockage. On a créé une interface `GameRepository` avec deux méthodes simples : `findAll()` pour récupérer les jeux et `save()` pour les enregistrer.

Ensuite, on a fait deux classes qui utilisent cette interface :

- `JsonGameRepository` : Elle utilise la librairie Jackson pour gérer le format JSON.
- `CsvGameRepository` : Elle lit et écrit le fichier ligne par ligne pour le format CSV.

Maintenant, la suite du programme pourra utiliser `GameRepository` sans se soucier de si c'est du JSON ou du CSV derrière.

Ensuite, Antonin s'est occupé de la logique métier avec la classe `GameService`. C'est elle qui gère la liste des jeux chargée depuis le stockage via le Repository. Elle contient les méthodes pour ajouter et supprimer des jeux, et surtout, c'est elle qui s'occupe de trier la liste par ordre alphabétique avant de la donner à l'affichage. Comme ça, le code de l'interface reste très simple.

Pour finir, Victorian s'est occupé de rédiger ce rapport et de faire le schéma de l'architecture. Par contre, pour trouver les problèmes du début et les solutions (comme le découpage en couches), on a tous réfléchi ensemble pour se mettre d'accord.



**Date :** 01/02/2025

Nous allons implémenter les 3 features : 
- No duplicates
- Recommended Games
- Week end Summary

et aussi des tests unitaires pour le game service et le game repository.

Victorian va s'occuper de la feature No duplicates, Ilan va s'occuper de la feature Recommended Games et Antonin va s'occuper de la feature Week end Summary.

### Feature Recommended Games (Ilan)

Pour cette feature, on a ajouté une méthode `recommendGame(int playerCount)` qui :
- Filtre les jeux compatibles avec le nombre de joueurs demandé (entre `minPlayers` et `maxPlayers`)
- Retourne un jeu aléatoire parmi les jeux compatibles
- Retourne `null` si aucun jeu ne correspond

On a mis cette logique dans `GameService` (la couche métier) et aussi dans `GameCollection` pour que ça marche avec le code actuel. Dans le menu, on a ajouté l'option 4 "Recommend Game" qui demande le nombre de joueurs et affiche une recommandation.

### Weekend Summary (Antonin)

Pour cette feature, on a implémenté un menu dynamique qui change selon le jour de la semaine :

**Fonctionnalités ajoutées :**

1. **Méthode `getRandomGames(int count)` dans `GameCollection` :**
   - Pioche entre 0 et 3 jeux aléatoires dans la collection
   - Gère automatiquement le cas où il ya moins de 3 jeux (affiche tous les jeux dispo)
   - Pas de doublons car chaque jeu pioché est retiré de la liste temporaire
   - Utilise `Math.random()` pour générer des index aléatoires et je cast avec int pour retirer les décimales

2. **Méthode `isWeekend()` dans `Menu` :**
   - Détecte si on est samedi ou dimanche avec `LocalDate` et `DayOfWeek`
   - Utilise la norme ISO 8601 (semaine commence le lundi)
   - Retourne `true` pour samedi/dimanche, `false` sinon

3. **Méthode `displayMainMenu()` modifiée :**
   - Menu dynamique : affiche 5 options en semaine et 6 options le weekend
   - En semaine : option 5 = Exit
   - Le weekend : option 5 = Weekend Summary et option 6 = Exit

4. **Méthode `displayWeekendSummary()` :**
   - Affiche entre 0 et 3 jeux random avec le format : `- Titre (min-max players, catégorie)`
   - Affiche le nombre réel de jeux trouvés dans le header
   - Gère le cas d'une collection vide avec un message adapté

5. **Méthode `handleMenu()` modifiée :**
   - Double switch selon le jour : un pour la semaine (5 cases) et un pour le weekend (6 cases)
   - Redirige vers `displayWeekendSummary()` quand l'utilisateur choisit l'option 5 le weekend
   - L'option Exit est décalée en position 6 uniquement le weekend

---

## Ajout feature No duplicates (Victorian)

### Prévention des doublons
- Ajout d'une exception métier `DuplicateGameException` (dans le package service) pour empêcher l'ajout de jeux avec le même nom (validation insensible à la casse).
- La vérification est faite dans `GameService` et aussi dans `GameCollection` pour garantir la cohérence selon le point d'entrée.
- L'interface utilisateur affiche un message d'erreur clair si un doublon est détecté.

### Tests unitaires
- Les tests de la fonctionnalité anti-doublons ont été ajoutés dans `GameServiceTest` (et déplacés dans le dossier `samplecode` pour respecter l'organisation demandée).
- Les tests vérifient :
  - L'ajout d'un jeu avec un nom déjà existant lève bien l'exception.
  - La validation fonctionne même si la casse est différente (ex : "Catan" et "CATAN").
  - La méthode `existsByTitle` fonctionne correctement.


**Date :** 08/02/2025

Cette semaine nous devons séparer le code en couches. Avec des layers différentes pour chaque fonctionnalité, comme vu en cours. Changer le switch pour le weekend. 
Modification du game service pour qu'il utilise le repository. Ajout d'un nouveau shema pour modéliser cette structure.
Ajout de test et des nouvelles features.
Plus ajout de la documentation.

## Ajout layers (Ilan)

Pour bien organiser le code et faire propre, j'ai tout séparé en plusieurs packages qui correspondent à des couches logiques (Layers). Ça permet de découpler les fonctionnalités :

- **fr.fges.presentation** : C'est la couche visible par l'utilisateur. Elle contient le `Menu` (qui gère l'affichage) et `InputHandler` (qui gère les entrées clavier).
- **fr.fges.application** : C'est la couche qui fait le lien. J'y ai mis les **Commandes** (Pattern Command). Chaque action du menu est une classe autonome (`AddGameCommand`, `RemoveGameCommand`...) qui fait le pont entre le menu et le service.
- **fr.fges.domain** : C'est le cœur du programme.
    - `service` : Contient `GameService` avec toute la logique métier (vérifications, règles).
    - `model` : Contient `BoardGame`, l'objet de données de base.
- **fr.fges.infrastructure** : C'est la couche technique. Elle contient les implémentations de sauvegarde (`CsvGameRepository`, `JsonGameRepository`).

Voici un schéma pour visualiser comment ça communique :

```mermaid
graph TD
    subgraph Presentation
        Menu
        InputHandler
    end
    subgraph Application
        Commandes[Commandes (Add, List...)]
    end
    subgraph Domain
        Service[GameService]
        Model[BoardGame]
    end
    subgraph Infrastructure
        Repo[Impémentations Repository (CSV/JSON)]
    end

    Menu -->|Utilise| InputHandler
    Menu -->|Exécute| Commandes
    Commandes -->|Appelle| Service
    Service -->|Manipule| Model
    Service -->|Sauvegarde via interface| Repo
```

L'avantage de cette structure, c'est que si demain on veut changer la façon de sauvegarder, on a juste à toucher à la couche **Infrastructure**. Le reste du code (Menu, Service) ne bougera pas. C'est le principe de l'injection de dépendances qu'on a mis en place dans le `Main`.
Plus ajout de la documentation.


**Date :** 22/02/2025

Shéma tornois :

'''mermaid
graph TD
    subgraph UI["Interface Tournoi"]
        TournamentUI["TournamentUI"]
        InputHandler["InputHandler"]
    end

    subgraph Commands["Commandes Tournoi"]
        CreateTournamentCmd["CreateTournamentCommand"]
        RunTournamentCmd["RunTournamentCommand"]
    end

    subgraph Services["Services"]
        TournamentService["TournamentService<br/>- createTournament<br/>- addPlayer"]
        TournamentOrchestrator["TournamentOrchestrator<br/>- runTournament<br/>- playMatch<br/>- determineChampion"]
    end

    subgraph TournamentLogic["Format Tournoi - Strategy Pattern"]
        TournamentFormat["TournamentFormat<br/>(Interface)<br/>- generateMatches<br/>- determineChampion"]
        ChampionshipFormat["ChampionshipFormat<br/>Tous vs Tous<br/>Système Points"]
        SingleElimination["SingleEliminationFormat<br/>Élimination Directe<br/>Bracket"]
        RoundRobinFormat["RoundRobinFormat<br/>Chaque joueur<br/>vs tous"]
    end

    subgraph Models["Modèles de Domaine"]
        Player["Player<br/>- id<br/>- name<br/>- wins<br/>- losses<br/>- getWinRate"]
        Tournament["Tournament<br/>- id<br/>- name<br/>- players<br/>- format<br/>- game<br/>- matches<br/>- champion<br/>- status"]
        Match["Match<br/>- player1<br/>- player2<br/>- game<br/>- winner<br/>- round"]
    end

    subgraph Existing["Utilise Existant"]
        GameService["GameService"]
        BoardGame["BoardGame"]
    end

    TournamentUI -->|utilise| InputHandler
    TournamentUI -->|affiche/lance| CreateTournamentCmd
    TournamentUI -->|affiche/lance| RunTournamentCmd
CreateTournamentCmd -->|délègue| TournamentService
    RunTournamentCmd -->|délègue| TournamentOrchestrator

    TournamentService -->|crée| Tournament
    TournamentService -->|utilise| BoardGame

    TournamentOrchestrator -->|utilise| TournamentFormat
    TournamentOrchestrator -->|génère| Match
    TournamentOrchestrator -->|manipule| Tournament
    TournamentOrchestrator -->|récupère les joueurs de| Tournament

    TournamentFormat -.->|implémente| ChampionshipFormat
    TournamentFormat -.->|implémente| SingleElimination
    TournamentFormat -.->|implémente| RoundRobinFormat

    Tournament -->|contient| Player
    Tournament -->|contient| Match
    Tournament -->|utilise| TournamentFormat
    Tournament -->|joue avec| BoardGame

    Match -->|oppose| Player
    Match -->|joue avec| BoardGame

    GameService -->|fournit| BoardGame

    style TournamentUI fill:#e1f5ff
    style CreateTournamentCmd fill:#fff3e0
    style RunTournamentCmd fill:#fff3e0
    style TournamentService fill:#f3e5f5
    style TournamentOrchestrator fill:#f3e5f5
    style TournamentFormat fill:#fce4ec
    style ChampionshipFormat fill:#fce4ec
    style SingleElimination fill:#fce4ec
    style RoundRobinFormat fill:#fce4ec
    style Player fill:#e8f5e9
    style Tournament fill:#e8f5e9
    style Match fill:#e8f5e9
'''

Implémentation du mode tournois :


**Refactoring Commandes & Menu (Ilan)**

J'ai changé la façon dont on gère les commandes dans le `Menu`. Avant on utilisait une `Map` avec les clés "1", "2"... c'était pas terrible si on voulait changer l'ordre ou ajouter un truc au milieu, fallait tout décaler à la main.

Du coup, j'ai tout passé en `List<Command>` !
Maintenant:
1. Le menu affiche les options automatiquement avec une boucle `for` (1., 2., 3...).
2. J'ai ajouté une méthode `getName()` dans l'interface `Command`. Comme ça chaque commande (Add, List, Remove...) donne son propre nom à afficher dans le menu.
3. J'ai renommé `AddGameCommand` en `AddAction` pour bien montrer que l'objet contient toute l'action (demander les infos à l'utilisateur + appeler le service).

C'est plus propre et plus facile à maintenir si on veut ajouter des options plus tard !


**Date :** 20/02/2026

## Simplification de l'affichage du Menu (Antonin)

Après avoir montré le projet au prof, il nous a fait trois remarques importantes :
1. Le cas pour le weekend est trop complexe
2. Les displays sont trop complexes
3. MenuDisplay doit rester une classe classique

On avait fait un peu trop avec le pattern Strategy pour quelque chose qui devait rester simple. Du coup j'ai fait un refactoring pour simplifier tout ça :

### Ce qui a changé :

**1. Transformation de MenuDisplay :**
- Avant : Une interface `MenuDisplay` avec deux implémentations séparées (`WeekendMenuDisplay` et `WeekdayMenuDisplay`)
- Après : Une seule classe simple `MenuDisplay` avec une méthode `display(boolean isWeekend)` qui prend un paramètre pour afficher le bon menu

**2. Simplification de Menu.java :**
- Suppression de la liste dynamique de commandes `List<Command>` qui se construisait selon le jour
- Remplacement par un simple `switch/case` qui vérifie `isWeekend` pour exécuter la bonne commande
- Suppression de la méthode `initializeCommands()` qui était trop complexe pour ce qu'elle faisait
- On garde juste `GameService` et on crée les commandes à la volée quand l'utilisateur choisit une option

**3. Correction d'ExitCommand :**
- J'ai ajouté `throw new MenuExitException()` dans la méthode `execute()` parce qu'elle était manquante et le programme ne se fermait jamais proprement

**4. Fichiers supprimés :**
- `WeekendMenuDisplay.java`
- `WeekdayMenuDisplay.java`

Le code est maintenant beaucoup plus lisible et direct. Au lieu d'avoir une architecture complexe pour afficher un menu, on a juste une méthode avec un if/else.

**Date :** 30/03/2026

## Refactoring Menu : Switch/Case vers Liste Dynamique (Antonin)

Après avoir simplifié le menu avec le switch/case, on s'est rendu compte qu'on pouvait le rendre encore plus flexible et maintenable. J'ai remplacé le switch/case par une **liste dynamique de commandes**.

### Ce qui a changé :

**1. Avant (Switch/Case) :**
```java
private void executeCommand(int option, boolean isWeekend) throws MenuExitException {
    switch (option) {
        case 1 -> new AddAction(...).execute();
        case 2 -> new RemoveGameCommand(...).execute();
        //etc...
        case 8 -> if (isWeekend) { ... } else { ... }
        case 9 -> if (isWeekend) { ... } else { ... }
        default -> ...
    }
}
```

**2. Après (Liste Dynamique) :**
```java
public Menu(GameService gameService, TournamentService tournamentService) {
    this.commands = new ArrayList<>();
    commands.add(new AddAction(...));
    commands.add(new RemoveGameCommand(...));
    //etc..
    commands.add(new ExitCommand());
}

public void handleMenu() throws MenuExitException {
    menuDisplay.display(commands);
    int option = Integer.parseInt(inputHandler.getInput());
    if (option >= 1 && option <= commands.size()) {
        commands.get(option - 1).execute();
    }
}
```

### Avantages du changement :

- **Plus simple** : Pas de switch/case ni conditions imbriquées
- **Plus maintenable** : Ajouter une commande = l'ajouter à la liste
- **Plus flexible** : Le nombre d'options s'ajuste automatiquement

### Modifications apportées :

**Menu.java :**
- Ajout de `List<Command> commands` initialisée dans le constructeur
- Suppression des méthodes `initializeCommands()`, `executeCommand()`, `isWeekend()`
- Simplification de `handleMenu()` : affiche → parse → exécute

**MenuDisplay.java :**
- Signature : `display(List<Command> commands)` au lieu de `display(boolean isWeekend)`
- Affichage dynamique : boucle for qui affiche chaque commande

## Dernières mises à jour (Ilan)

**Séparation de l'UI et du Domaine (Single Responsibility Principle & MVC)** : J'ai retiré la méthode `getFormatName()` de l'interface `TournamentFormat` et de ses implémentations (`RoundRobinFormat` et `KingOfTheHillFormat`). Le domaine n'a pas à gérer le texte affiché pour l'utilisateur. Toute la partie UI est désormais centralisée dans le `TournamentFormatter` au niveau de la couche de présentation. Cela respecte le SRP (Principe de Responsabilité Unique) car la logique métier du format de tournoi n'est plus couplée à l'affichage.

**Amélioration du pattern Strategy (Stratégie)** : L'interface `TournamentFormat` agit comme le pattern *Strategy*, permettant d'interchanger les comportements de tournoi (`RoundRobin`, `KingOfTheHill`) de façon dynamique. J'ai nettoyé le pattern pour qu'il se concentre uniquement sur son rôle algorithmique (`getNextMatch` et `generateMatches`). 

**Refactorisation du format King of the Hill (Boucle `do-while`)** : J'ai modifié la logique pour trouver le prochain adversaire dans l'algorithme `KingOfTheHillFormat`. L'ancienne méthode `findNextOpponent` était trop complexe avec ses streams. Je l'ai remplacée par une simple boucle `do-while` directement dans `getNextMatch`. Ça simule parfaitement une file d'attente circulaire (le perdant repart en fin de file) en lisant simplement l'index. L'algorithme de notre *Strategy* est maintenant beaucoup plus clair et performant !

## Dernières mises à jour (Victorian)

- **Correction de bugs** (`e9a63d9`, `35e1d23`)
- **Implémentation complète de tournoi (sans les tests)** (`a451bde`)
- **Ajout de la fonctionnalité de recherche de partie par nombre de joueurs** (`183c9ad`) : Ajout de la fonctionnalité et des tests correspondants.
- **Ajout de fonctionnalités et tests** (`eaf16d9`) : Ajout de tests et mise à jour du rapport pour correspondre aux tests.
- **Gestion des erreurs/exceptions** (`3522d51`) : Ajout d'une meilleure gestion des exceptions.