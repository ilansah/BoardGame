# Output Examples

## Starting the Application

### With JSON storage
```bash
$ ./run.sh games.json
Using storage file: games.json

=== Board Game Collection ===
1. Add Board Game
2. Remove Board Game
3. List All Board Games
4. Exit
Please select an option (1-4):
```

### With CSV storage
```bash
$ ./run.sh games.csv
Using storage file: games.csv

=== Board Game Collection ===
1. Add Board Game
2. Remove Board Game
3. List All Board Games
4. Exit
Please select an option (1-4):
```

### With invalid file extension
```bash
$ ./run.sh games.txt
Error: Storage file must have .json or .csv extension
```

### Without arguments
```bash
$ ./run.sh
Usage: java -jar boardgamecollection.jar <storage-file>
Storage file must be .json or .csv
```

## Adding a Board Game

```
=== Board Game Collection ===
1. Add Board Game
2. Remove Board Game
3. List All Board Games
4. Exit
Please select an option (1-4):

1
Title: Catan
Minimum Players: 3
Maximum Players: 4
Category (e.g., fantasy, cooperative, family, strategy): strategy
Board game added successfully.

=== Board Game Collection ===
1. Add Board Game
2. Remove Board Game
3. List All Board Games
4. Exit
Please select an option (1-4):

1
Title: Pandemic
Minimum Players: 2
Maximum Players: 4
Category (e.g., fantasy, cooperative, family, strategy): cooperative
Board game added successfully.
```

## Listing All Board Games

```
=== Board Game Collection ===
1. Add Board Game
2. Remove Board Game
3. List All Board Games
4. Exit
Please select an option (1-4):

3
Game: Catan (3-4 players) - strategy
Game: Pandemic (2-4 players) - cooperative
```

### When collection is empty
```
=== Board Game Collection ===
1. Add Board Game
2. Remove Board Game
3. List All Board Games
4. Exit
Please select an option (1-4):

3
No board games in collection.
```

## Removing a Board Game

### Successful removal
```
=== Board Game Collection ===
1. Add Board Game
2. Remove Board Game
3. List All Board Games
4. Exit
Please select an option (1-4):

2
Title of game to remove: Catan
Board game removed successfully.
```

### Game not found
```
=== Board Game Collection ===
1. Add Board Game
2. Remove Board Game
3. List All Board Games
4. Exit
Please select an option (1-4):

2
Title of game to remove: Monopoly
No board game found with that title.
```

## Exiting the Application

```
=== Board Game Collection ===
1. Add Board Game
2. Remove Board Game
3. List All Board Games
4. Exit
Please select an option (1-4):

4
Exiting the application. Goodbye!
```

## Storage File Formats

### JSON format (games.json)
```json
[
  {
    "title": "Catan",
    "minPlayers": 3,
    "maxPlayers": 4,
    "category": "strategy"
  },
  {
    "title": "Pandemic",
    "minPlayers": 2,
    "maxPlayers": 4,
    "category": "cooperative"
  },
  {
    "title": "Ticket to Ride",
    "minPlayers": 2,
    "maxPlayers": 5,
    "category": "family"
  }
]
```

### CSV format (games.csv)
```csv
title,minPlayers,maxPlayers,category
Catan,3,4,strategy
Pandemic,2,4,cooperative
Ticket to Ride,2,5,family
```
