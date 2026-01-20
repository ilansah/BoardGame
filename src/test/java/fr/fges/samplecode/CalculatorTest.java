package fr.fges.samplecode;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Calculator class.
 * 
 * This demonstrates:
 * - Testing behavior, not implementation
 * - Small focused tests (one assertion per concept)
 * - Test names starting with "should"
 * - AAA pattern (Arrange, Act, Assert)
 * - Testing domain objects directly (no mocking)
 */
class CalculatorTest {

    // Constants at the top of the file
    private static final int OPERAND_A = 10;
    private static final int OPERAND_B = 5;

    // ========== ADD TESTS ==========

    @Test
    void shouldAddTwoPositiveNumbers() {
        // Arrange
        Calculator calculator = new Calculator();

        // Act
        int result = calculator.add(OPERAND_A, OPERAND_B);

        // Assert
        assertEquals(15, result);
    }

    @Test
    void shouldAddNegativeNumbers() {
        // Arrange
        Calculator calculator = new Calculator();

        // Act
        int result = calculator.add(-5, -3);

        // Assert
        assertEquals(-8, result);
    }

    @Test
    void shouldReturnSameNumberWhenAddingZero() {
        // Arrange
        Calculator calculator = new Calculator();

        // Act
        int result = calculator.add(OPERAND_A, 0);

        // Assert
        assertEquals(OPERAND_A, result);
    }

    // ========== SUBTRACT TESTS ==========

    @Test
    void shouldSubtractTwoNumbers() {
        // Arrange
        Calculator calculator = new Calculator();

        // Act
        int result = calculator.subtract(OPERAND_A, OPERAND_B);

        // Assert
        assertEquals(5, result);
    }

    @Test
    void shouldReturnNegativeWhenSubtractingLargerFromSmaller() {
        // Arrange
        Calculator calculator = new Calculator();

        // Act
        int result = calculator.subtract(OPERAND_B, OPERAND_A);

        // Assert
        assertEquals(-5, result);
    }

    // ========== MULTIPLY TESTS ==========

    @Test
    void shouldMultiplyTwoNumbers() {
        // Arrange
        Calculator calculator = new Calculator();

        // Act
        int result = calculator.multiply(OPERAND_A, OPERAND_B);

        // Assert
        assertEquals(50, result);
    }

    @Test
    void shouldReturnZeroWhenMultiplyingByZero() {
        // Arrange
        Calculator calculator = new Calculator();

        // Act
        int result = calculator.multiply(OPERAND_A, 0);

        // Assert
        assertEquals(0, result);
    }

    // ========== DIVIDE TESTS ==========

    @Test
    void shouldDivideTwoNumbers() {
        // Arrange
        Calculator calculator = new Calculator();

        // Act
        int result = calculator.divide(OPERAND_A, OPERAND_B);

        // Assert
        assertEquals(2, result);
    }

    @Test
    void shouldThrowExceptionWhenDividingByZero() {
        // Arrange
        Calculator calculator = new Calculator();

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            calculator.divide(OPERAND_A, 0);
        });
    }
}
