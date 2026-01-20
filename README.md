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
