package fr.fges.samplecode;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the CalculatorService class.
 * 
 * This demonstrates:
 * - Mocking collaborators (repository), NOT domain objects (calculator)
 * - Testing behavior, not implementation
 * - Small focused tests
 * - Test names starting with "should"
 * - AAA pattern (Arrange, Act, Assert)
 * - Using real domain objects in tests
 * - Helper methods at the bottom of the file to create test data
 */
class CalculatorServiceTest {

    // Constants at the top of the file
    private static final int OPERAND_A = 10;
    private static final int OPERAND_B = 5;

    // Dependencies
    private Calculator calculator;
    private CalculatorRepository mockRepository;
    private CalculatorService service;

    @BeforeEach
    void setUp() {
        // Use REAL domain object (Calculator) - never mock domain objects!
        calculator = new Calculator();

        // Mock external dependency (Repository) - this is what we should mock
        mockRepository = mock(CalculatorRepository.class);

        // Create service with real domain object and mocked external dependency
        service = new CalculatorService(calculator, mockRepository);
    }

    // ========== ADD AND SAVE TESTS ==========

    @Test
    void shouldReturnCorrectResultWhenAdding() {
        // Arrange - nothing extra needed, setUp handles it

        // Act
        int result = service.addAndSave(OPERAND_A, OPERAND_B);

        // Assert
        assertEquals(15, result);
    }

    @Test
    void shouldSaveResultToRepositoryWhenAdding() {
        // Arrange - nothing extra needed

        // Act
        service.addAndSave(OPERAND_A, OPERAND_B);

        // Assert - verify the repository was called
        verify(mockRepository).save(any(CalculationResult.class));
    }

    // ========== SUBTRACT AND SAVE TESTS ==========

    @Test
    void shouldReturnCorrectResultWhenSubtracting() {
        // Arrange - nothing extra needed

        // Act
        int result = service.subtractAndSave(OPERAND_A, OPERAND_B);

        // Assert
        assertEquals(5, result);
    }

    @Test
    void shouldSaveResultToRepositoryWhenSubtracting() {
        // Arrange - nothing extra needed

        // Act
        service.subtractAndSave(OPERAND_A, OPERAND_B);

        // Assert
        verify(mockRepository).save(any(CalculationResult.class));
    }

    // ========== GET HISTORY TESTS ==========

    @Test
    void shouldReturnHistoryFromRepository() {
        // Arrange
        List<CalculationResult> expectedHistory = List.of(
                createCalculationResult("1", "ADD", 2, 3, 5),
                createCalculationResult("2", "SUBTRACT", 10, 4, 6)
        );
        when(mockRepository.findAll()).thenReturn(expectedHistory);

        // Act
        List<CalculationResult> history = service.getHistory();

        // Assert
        assertEquals(2, history.size());
        assertEquals("ADD", history.get(0).operation());
        assertEquals("SUBTRACT", history.get(1).operation());
    }

    @Test
    void shouldReturnEmptyListWhenNoHistory() {
        // Arrange
        when(mockRepository.findAll()).thenReturn(List.of());

        // Act
        List<CalculationResult> history = service.getHistory();

        // Assert
        assertTrue(history.isEmpty());
    }

    // ========== SUM ALL RESULTS TESTS ==========

    @Test
    void shouldSumAllResultsFromRepository() {
        // Arrange
        List<CalculationResult> results = List.of(
                createCalculationResult("1", "ADD", 2, 3, 5),
                createCalculationResult("2", "ADD", 4, 6, 10),
                createCalculationResult("3", "ADD", 1, 1, 2)
        );
        when(mockRepository.findAll()).thenReturn(results);

        // Act
        int sum = service.sumAllResults();

        // Assert
        assertEquals(17, sum); // 5 + 10 + 2 = 17
    }

    @Test
    void shouldReturnZeroWhenNoResultsToSum() {
        // Arrange
        when(mockRepository.findAll()).thenReturn(List.of());

        // Act
        int sum = service.sumAllResults();

        // Assert
        assertEquals(0, sum);
    }

    // ========== HELPER METHODS (at the bottom of the file) ==========

    /**
     * Helper method to create CalculationResult instances for tests.
     * This avoids mocking domain objects and creates real instances instead.
     */
    private CalculationResult createCalculationResult(String id, String operation, int op1, int op2, int result) {
        return new CalculationResult(id, operation, op1, op2, result);
    }
}
