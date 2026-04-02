# Architecture Cible du Projet BoardGame

Ce document décrit la structure de dossier recommandée pour garder une séparation nette entre le métier et la technique.

## Principe général

Le projet doit suivre une séparation en couches simple:

- `domain` contient le coeur métier
- `application` orchestre les cas d'usage
- `presentation` gère l'interface utilisateur
- `infrastructure` gère les détails techniques comme les fichiers

L'objectif est que le code métier ne dépende pas du code technique.

## Structure recommandée

```text
src/main/java/fr/fges/
  Main.java
  domain/
    model/
    port/
    service/
  application/
    command/
  presentation/
  infrastructure/
    repository/
```

## Rôle de chaque dossier

### `domain/model`

Contient les objets métier et les valeurs du domaine.

Exemples:

- `BoardGame`
- `Tournament`
- `Player`
- `Match`
- `Action`
- `ActionHistory`

Règle: ces classes ne doivent pas dépendre de l'UI ni du stockage.

### `domain/service`

Contient la logique métier pure.

Exemples:

- `GameService`
- `GameManagementService`
- `GameRecommendationService`
- `GameHistoryService`
- `TournamentService`

Règle: un service métier ne doit pas afficher du texte à l'utilisateur.

### `domain/port`

Contient les contrats que le domaine attend.

Exemples:

- `GameRepository`
- `GameMutationPort`

Règle: le domaine définit le besoin, l'infrastructure fournit l'implémentation.

### `application/command`

Contient les cas d'usage orientés interface.

Exemples:

- `AddAction`
- `RemoveGameCommand`
- `ListGamesCommand`
- `RecommendGameCommand`
- `UndoCommand`

Règle: ces classes orchestrent l'entrée utilisateur et appellent les services.

### `presentation`

Contient l'affichage et la saisie utilisateur.

Exemples:

- `Menu`
- `MenuDisplay`
- `InputHandler`
- `TournamentFormatter`

Règle: cette couche parle à l'utilisateur, pas au stockage.

### `infrastructure/repository`

Contient les implémentations techniques du stockage.

Exemples:

- `CsvGameRepository`
- `JsonGameRepository`

Règle: cette couche dépend du domaine, jamais l'inverse.

## Structure recommandée pour les tests

```text
src/test/java/fr/fges/
  domain/
    service/
      facade/
      history/
      management/
      recommendation/
  samplecode/
```

### Idée de découpage des tests

- `domain/service/management` pour le CRUD et les règles de base
- `domain/service/recommendation` pour les recherches et recommandations
- `domain/service/history` pour l'undo et l'historique
- `domain/service/facade` pour vérifier l'assemblage de `GameService`

## Ce que cette structure améliore

1. Les fichiers sont plus petits et plus lisibles.
2. Les responsabilités sont claires.
3. Les tests sont plus faciles à retrouver.
4. Le métier devient plus simple à faire évoluer.
5. La technique peut changer sans casser le coeur du projet.

## Règle pratique simple

Si une classe parle à l'utilisateur, elle va dans `presentation`.

Si une classe contient une règle métier, elle va dans `domain`.

Si une classe lit ou écrit un fichier, elle va dans `infrastructure`.

Si une classe sert à relier une action UI à un service métier, elle va dans `application`.