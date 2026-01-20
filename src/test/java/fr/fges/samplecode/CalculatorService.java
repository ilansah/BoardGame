package fr.fges.samplecode;

import java.util.List;
import java.util.UUID;

/**
 * Service that uses Calculator (domain) and CalculatorRepository (external dependency).
 * This demonstrates how services coordinate between domain objects and external dependencies.
 */
public class CalculatorService {

    private final Calculator calculator;
    private final CalculatorRepository repository;

    public CalculatorService(Calculator calculator, CalculatorRepository repository) {
        this.calculator = calculator;
        this.repository = repository;
    }

    public int addAndSave(int a, int b) {
        int result = calculator.add(a, b);
        CalculationResult calculationResult = new CalculationResult(
                UUID.randomUUID().toString(),
                "ADD",
                a,
                b,
                result
        );
        repository.save(calculationResult);
        return result;
    }

    public int subtractAndSave(int a, int b) {
        int result = calculator.subtract(a, b);
        CalculationResult calculationResult = new CalculationResult(
                UUID.randomUUID().toString(),
                "SUBTRACT",
                a,
                b,
                result
        );
        repository.save(calculationResult);
        return result;
    }

    public List<CalculationResult> getHistory() {
        return repository.findAll();
    }

    public int sumAllResults() {
        List<CalculationResult> results = repository.findAll();
        int sum = 0;
        for (CalculationResult result : results) {
            sum = calculator.add(sum, result.result());
        }
        return sum;
    }
}
