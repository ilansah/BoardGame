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

Victorian va s'occuper de la feature No duplicates, Ilan va s'occuper de la feature Recommended Games et Antonin va s'occuper de la feature Week end Summary.

-