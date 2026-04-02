package fr.fges.application.command;

import fr.fges.domain.service.GameService;
import fr.fges.presentation.InputHandler;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.mockito.Mockito.mock;

/**
 * Support commun pour les tests des commandes applicatives.
 */
public abstract class CommandTestSupport {
    protected GameService gameService;
    protected InputHandler inputHandler;

    private PrintStream originalOut;
    protected ByteArrayOutputStream outputStream;

    protected void setUpCommon() {
        gameService = mock(GameService.class);
        inputHandler = mock(InputHandler.class);
        outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
    }

    protected String getConsoleOutput() {
        return outputStream.toString().replace("\r\n", "\n");
    }

    protected void restoreConsole() {
        System.setOut(originalOut);
    }
}