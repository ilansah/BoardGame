# Board Game Collection

A CLI application to manage a board game collection. This project intentionally demonstrates **bad design patterns** for educational purposes (SRP violations, static state, mixed responsibilities).

## Requirements

- Java 21+
- Maven 3.9+

## How to Run

### Using the run script

```bash
# On Linux/Mac
./run.sh games.json

# On Windows
run.bat games.json
```

### Using Maven directly

```bash
# Compile
./mvnw compile

# Run with JSON storage
./mvnw exec:java -Dexec.mainClass="fr.fges.Main" -Dexec.args="games.json"

# Run with CSV storage
./mvnw exec:java -Dexec.mainClass="fr.fges.Main" -Dexec.args="games.csv"
```

### Using Makefile

```bash
make run ARGS=games.json
```

## Running Tests

```bash
./mvnw test
```

## Storage Formats

The application supports two storage formats:
- **JSON** (`.json` extension)
- **CSV** (`.csv` extension)

The storage file is passed as a command-line argument at startup.

## Documentation

- [Output Examples](docs/output-example.md) - Example CLI sessions
- [Architecture Cible](docs/architecture.md) - Structure recommandée du projet et des tests

## Dernières mises à jour (Ilan)

    1. **Séparation de l'UI et du Domaine (Single Responsibility Principle & MVC)** : J'ai retiré la méthode `getFormatName()` de l'interface `TournamentFormat` et de ses implémentations (`RoundRobinFormat` et `KingOfTheHillFormat`). Le domaine n'a pas à gérer le texte affiché pour l'utilisateur. Toute la partie UI est désormais centralisée dans le `TournamentFormatter` au niveau de la couche de présentation. Cela respecte le SRP (Principe de Responsabilité Unique) car la logique métier du format de tournoi n'est plus couplée à l'affichage.

    2. **Amélioration du pattern Strategy (Stratégie)** : L'interface `TournamentFormat` agit comme le pattern *Strategy*, permettant d'interchanger les comportements de tournoi (`RoundRobin`, `KingOfTheHill`) de façon dynamique. J'ai nettoyé le pattern pour qu'il se concentre uniquement sur son rôle algorithmique (`getNextMatch` et `generateMatches`). 

    3. **Refactorisation du format King of the Hill (Boucle `do-while`)** : J'ai modifié la logique pour trouver le prochain adversaire dans l'algorithme `KingOfTheHillFormat`. L'ancienne méthode `findNextOpponent` était trop complexe avec ses streams. Je l'ai remplacée par une simple boucle `do-while` directement dans `getNextMatch`. Ça simule parfaitement une file d'attente circulaire (le perdant repart en fin de file) en lisant simplement l'index. L'algorithme de notre *Strategy* est maintenant beaucoup plus clair et performant !
