#!/bin/bash
./mvnw -q compile exec:java -Dexec.mainClass="fr.fges.Main" -Dexec.args="$*"
