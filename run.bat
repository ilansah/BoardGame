@echo off
mvnw.cmd -q compile exec:java -Dexec.mainClass="fr.fges.Main" -Dexec.args="%*"
