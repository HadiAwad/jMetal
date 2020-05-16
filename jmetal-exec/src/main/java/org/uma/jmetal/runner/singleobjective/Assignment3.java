package org.uma.jmetal.runner.singleobjective;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.singleobjective.geneticalgorithm.GeneticAlgorithmBuilder;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection;
import org.uma.jmetal.problem.singleobjective.OneMax;
import org.uma.jmetal.solution.BinarySolution;
import org.uma.jmetal.solution.PermutationSolution;
import org.uma.jmetal.util.JMetalLogger;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.uma.jmetal.bzu.Utility.*;

/**
 * Class to configure and run a generational genetic algorithm. The target problem is OneMax.
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */
public class Assignment3 {

    public static void main(String[] args) throws Exception {
      JMetalLogger.logger.info("************************* Start **********************");
      double[] crArray = new double[]{0.7, 0.8, 0.9};
      int[] popArray = new int[]{50, 100, 150};
      int numberOfRuns = 10;
      int numberOfBits =1024;
      double[] mArray = new double[]{1.0/numberOfBits, 2.0/numberOfBits, 3.0/numberOfBits};

      SelectionOperator<List<BinarySolution>, BinarySolution> selection =
              new BinaryTournamentSelection<>();

      writeToCSVHeader();
      List<Algorithm<PermutationSolution<Integer>>> algorithmList =
              buildAssignment3Experiments(  new OneMax(numberOfBits) , GeneticAlgorithmBuilder.GeneticAlgorithmVariant.STEADY_STATE,
                      crArray,popArray,mArray,numberOfRuns,selection);

      Stream<Map.Entry<String, Double>> sorted =
              myMAp.entrySet().stream()
                      .sorted(Map.Entry.comparingByValue());

      sorted.forEach(System.out::println);

      JMetalLogger.logger.info("************************* END **********************");

    }
}
