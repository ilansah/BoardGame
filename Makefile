.PHONY: compile run test clean

compile:
	./mvnw compile

run:
	./mvnw -q compile exec:java -Dexec.mainClass="fr.fges.Main" -Dexec.args="$(ARGS)"

test:
	./mvnw test

clean:
	./mvnw clean
