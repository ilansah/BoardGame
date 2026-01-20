package fr.fges.samplecode;

/**
 * A record representing a calculation result.
 * This is a domain model that should NOT be mocked in tests.
 */
public record CalculationResult(
        String id,
        String operation,
        int operand1,
        int operand2,
        int result
) {
}
