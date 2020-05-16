package org.uma.jmetal.runner.singleobjective;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection;
import org.uma.jmetal.problem.singleobjective.TSP;
import org.uma.jmetal.solution.PermutationSolution;

import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.comparator.RankingAndCrowdingDistanceComparator;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Stream;
import static org.uma.jmetal.bzu.Utility.buildAssginment2Experiments;
import static org.uma.jmetal.bzu.Utility.writeToCSVHeader;
import static org.uma.jmetal.bzu.Utility.myMAp;

/**
 * Class to configure and run a generational genetic algorithm. The target problem is TSP.
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */
public class GenerationalGeneticAlgorithmTSPRunnerHadi {
  /**
   * Usage: java org.uma.jmetal.runner.singleobjective.BinaryGenerationalGeneticAlgorithmRunner
   *
   * @return
   */

  public static void main(String[] args) throws Exception {
    JMetalLogger.logger.info("*************************Start**********************");
    double[] crArray = new double[]{0.5, 0.6, 0.7, 0.8, 0.9};
    int[] popArray = new int[]{50, 100, 150, 200};
    double[] mArray = new double[]{0.05, 0.1, 0.2, 0.3};
    int numberOfRuns = 20;


    SelectionOperator<List<PermutationSolution<Integer>>, PermutationSolution<Integer>> selection =
            new BinaryTournamentSelection<PermutationSolution<Integer>>(new RankingAndCrowdingDistanceComparator<PermutationSolution<Integer>>());


    writeToCSVHeader();
    List<Algorithm<PermutationSolution<Integer>>> algorithmList =
            buildAssginment2Experiments(new TSP("/tspInstances/kroA100.tsp"),crArray,popArray,mArray,numberOfRuns,selection);

    Stream<Entry<String, Double>> sorted =
            myMAp.entrySet().stream()
                    .sorted(Map.Entry.comparingByValue());

    sorted.forEach(System.out::println);

    JMetalLogger.logger.info("*************************END**********************");
//    int numberOfRuns = 20;
//
//    for (Algorithm<PermutationSolution<Integer>> algorithm : algorithmList) {
//      runAlgorithm(algorithm, numberOfRuns);
//    }

  }

}






