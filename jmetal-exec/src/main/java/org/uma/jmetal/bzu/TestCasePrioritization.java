package org.uma.jmetal.bzu;

import org.uma.jmetal.bzu.coverage.AveragePercentageCoverage;
import org.uma.jmetal.bzu.coverage.CoverageMatrix;
import org.uma.jmetal.bzu.coverage.ExecutionCostVector;
import org.uma.jmetal.problem.impl.AbstractIntegerPermutationProblem;
import org.uma.jmetal.solution.PermutationSolution;
import org.uma.jmetal.util.JMetalLogger;

import java.io.IOException;

public class TestCasePrioritization extends AbstractIntegerPermutationProblem {

    private int numberOfTestCases;
    private ExecutionCostVector executionCostVector = null;
    private ExecutionCostVector onlyOneCostMatrix = null;
    private CoverageMatrix faultCoverageMatrix;
    private CoverageMatrix branchCoverageMatrix;

    public TestCasePrioritization(String distanceFolder,boolean costUnified) throws IOException {
        //executionCostVector = new ExecutionCostVector(distanceFolder+"/cost.csv");
        //onlyOneCostMatrix = new ExecutionCostVector(distanceFolder+"/cost1.csv");

        if(costUnified){
            JMetalLogger.logger.info("COST1 is used ");
            executionCostVector = new ExecutionCostVector(distanceFolder+"/cost1.csv");
        }else{
            JMetalLogger.logger.info("Real COST is used ");
            executionCostVector = new ExecutionCostVector(distanceFolder+"/cost.csv");
        }

        faultCoverageMatrix = new CoverageMatrix(distanceFolder+"/past_faults.csv");
        branchCoverageMatrix = new CoverageMatrix(distanceFolder+"/coverage.csv");
        numberOfTestCases = executionCostVector.size();

        setNumberOfVariables(numberOfTestCases);
        setNumberOfObjectives(2);
        setName("TestCasePrioritization");
    }

    @Override public int getPermutationLength() {
        return numberOfTestCases ;
    }

    @Override
    public void evaluate(PermutationSolution<Integer> solution) {
        double faultFitness = AveragePercentageCoverage.calculate(solution, faultCoverageMatrix,executionCostVector);
        double branchFitness = AveragePercentageCoverage.calculate(solution, branchCoverageMatrix,executionCostVector);
        //System.out.println("evaluate " +faultFitness +"---"+ branchFitness );
        solution.setObjective(0,  -1.0 *faultFitness);
        solution.setObjective(1,  -1.0 *branchFitness);
    }


}
