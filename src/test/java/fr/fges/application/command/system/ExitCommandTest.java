package fr.fges.application.command.system;

import fr.fges.application.command.CommandTestSupport;
import fr.fges.exceptions.MenuExitException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("ExitCommand Tests")
class ExitCommandTest extends CommandTestSupport {

    @AfterEach
    void tearDown() {
        restoreConsole();
    }

    @Test
    @DisplayName("getName doit retourner le libelle de la commande")
    void getName_shouldReturnCommandLabel() {
        assertEquals("Exit", new ExitCommand().getName());
    }

    @Test
    @DisplayName("execute doit lever MenuExitException")
    void execute_shouldThrowMenuExitException() {
        ExitCommand command = new ExitCommand();

        assertThrows(MenuExitException.class, command::execute);
    }
}