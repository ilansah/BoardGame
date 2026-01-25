# Rapport de Refactoring - Projet BoardGame

**Groupe :** Ilan, Antonin, Victorian
**Date :** 25/01/2025


## 1. Analyse de l'existant

En regardant le code actuel, on a vu que c'était assez mélangé. Les différentes parties du code dépendent trop les unes des autres :

* **GameCollection :** C'est la classe qui pose problème car elle fait tout toute seule. Elle gère la liste des jeux, la sauvegarde des fichiers et même l'affichage à l'écran.
* **BoardGame :** C'est juste le format des données, cette partie est très bien.
* **Menu :** Elle s'occupe de l'affichage, mais elle va chercher les informations directement dans `GameCollection` au lieu de passer par un intermédiaire.
* **Main :** Il sert juste à lancer le programme.

**Le vrai problème :** Si on veut changer la façon de sauvegarder ou l'affichage, on est obligé de modifier la classe `GameCollection` qui est déjà trop chargée.

## 2. Architecture proposée

Pour régler ça, on a décidé de découper le code en trois parties bien distinctes. Chacune aura un rôle précis :

1.  **Le Stockage (Repository) :** S'occupe uniquement de lire et écrire les fichiers de sauvegarde (CSV ou JSON).
2.  **La Logique (Service) :** S'occupe de gérer les jeux (ajouter, supprimer, trier la liste).
3.  **L'Interface (Controller/View) :** S'occupe de ce que l'utilisateur voit et de ce qu'il tape au clavier.
