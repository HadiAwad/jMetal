package org.uma.jmetal.bzu;

import org.uma.jmetal.bzu.coverage.AveragePercentageCoverage;
import org.uma.jmetal.bzu.coverage.CoverageMatrix;
import org.uma.jmetal.bzu.coverage.ExecutionCostVector;
import org.uma.jmetal.problem.impl.AbstractIntegerPermutationProblem;
import org.uma.jmetal.solution.PermutationSolution;

import java.io.IOException;

public class TestCasePrioritization_Single extends AbstractIntegerPermutationProblem {

    private int numberOfTestCases;
    private ExecutionCostVector executionCostVector = null;
    private CoverageMatrix coverageMatrix;

    public TestCasePrioritization_Single(String distanceFolder) throws IOException {
        //executionCostVector = new ExecutionCostVector(distanceFolder+"/cost.csv");
        executionCostVector = new ExecutionCostVector(distanceFolder+"/cost1.csv");
        coverageMatrix = new CoverageMatrix(distanceFolder+"/past_faults.csv");
        numberOfTestCases = executionCostVector.size();

        setNumberOfVariables(numberOfTestCases);
        setNumberOfObjectives(1);
        setName("TestCasePrioritization_Single");
    }

    @Override public int getPermutationLength() {
        return numberOfTestCases ;
    }

    @Override
    public void evaluate(PermutationSolution<Integer> solution) {
        double fitness = AveragePercentageCoverage.calculate(solution,coverageMatrix,executionCostVector);
        System.out.println(fitness+"-"+-1.0 *fitness);
        solution.setObjective(0,  -1.0 *fitness);
    }

    public Integer getUpperBound(int index) {
        return numberOfTestCases;
    }

    public Integer getLowerBound(int index) {
        return 0;
    }

}
