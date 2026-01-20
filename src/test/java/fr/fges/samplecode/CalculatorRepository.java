package fr.fges.samplecode;

import java.util.List;

/**
 * Repository interface for storing calculation results.
 * This is an external dependency (collaborator) that SHOULD be mocked in tests.
 */
public interface CalculatorRepository {

    void save(CalculationResult result);

    List<CalculationResult> findAll();

    CalculationResult findById(String id);
}
