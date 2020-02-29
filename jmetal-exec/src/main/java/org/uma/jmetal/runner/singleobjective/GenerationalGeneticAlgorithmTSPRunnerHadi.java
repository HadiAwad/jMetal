package org.uma.jmetal.runner.singleobjective;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.singleobjective.geneticalgorithm.GeneticAlgorithmBuilder;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.crossover.PMXCrossover;
import org.uma.jmetal.operator.impl.mutation.PermutationSwapMutation;
import org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection;
import org.uma.jmetal.problem.PermutationProblem;
import org.uma.jmetal.problem.singleobjective.TSP;
import org.uma.jmetal.solution.PermutationSolution;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.comparator.RankingAndCrowdingDistanceComparator;
import org.uma.jmetal.algorithm.singleobjective.geneticalgorithm.GenerationalGeneticAlgorithm;

import org.uma.jmetal.util.JMetalLogger;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.Map.Entry;
import java.util.stream.Stream;

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

  private  static Map<String,Double> myMAp = new HashMap<>();

  public static List<Algorithm<PermutationSolution<Integer>>> buildExperiments(PermutationProblem<PermutationSolution<Integer>> problem) throws Exception {

    double[] crArray = new double[]{0.5, 0.6, 0.7, 0.8, 0.9};
    int[] popArray = new int[]{50, 100, 150, 200};
    double[] mArray = new double[]{0.05, 0.1, 0.2, 0.3};
    int numberOfRuns = 20;
    SelectionOperator<List<PermutationSolution<Integer>>, PermutationSolution<Integer>> selection =
            new BinaryTournamentSelection<PermutationSolution<Integer>>(new RankingAndCrowdingDistanceComparator<PermutationSolution<Integer>>());

    List<Algorithm<PermutationSolution<Integer>>> algorithmList = new ArrayList<>();
    int runId = 0;

    for (double cr : crArray) {
      CrossoverOperator<PermutationSolution<Integer>> crossover = new PMXCrossover(cr);
      for (double mutaion : mArray) {
        MutationOperator<PermutationSolution<Integer>> mutation = new PermutationSwapMutation<Integer>(mutaion);
        for (int popSize : popArray) {
          System.out.println(runId++ + "--" + cr + "--" + popSize + "--" + mutaion);
          Algorithm<PermutationSolution<Integer>> algorithm = new GeneticAlgorithmBuilder<>(problem, crossover, mutation)
                  .setPopulationSize(popSize)
                  .setMaxEvaluations(30000000)
                  .setSelectionOperator(selection)
                  .build();
          runAlgorithm(algorithm,numberOfRuns,runId,cr,mutaion,popSize);
        }
      }
    }


    return algorithmList;
  }

  public static void main(String[] args) throws Exception {
    JMetalLogger.logger.info("*************************Start**********************");
    writeToCSVHeader();
    List<Algorithm<PermutationSolution<Integer>>> algorithmList =
            buildExperiments(new TSP("/tspInstances/kroA100.tsp"));

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

  private static void runAlgorithm(Algorithm<PermutationSolution<Integer>> algorithm, int numberOfRuns, int runId,
                                   double cr, double mutaion, int popSize) {
    int runs = 0;
    for (runs = 0; runs < numberOfRuns; runs++) {

      AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm)
              .execute();

      PermutationSolution<Integer> solution = algorithm.getResult();

      long computingTime = algorithmRunner.getComputingTime();
      int evaluations = ((GenerationalGeneticAlgorithm) algorithm).getNumberOfEvaluations();

      writeToCSV(runId,runs,cr,mutaion,popSize,evaluations,computingTime,solution.getObjective(0));

    }

  }

  private static void writeToCSV(int run, int trial, double crossOver, double mutation, int popSize, long evluations,
                                 long time, double solution) {

    try (PrintWriter pw = new PrintWriter(new FileOutputStream(new File("experimentRuns.csv"), true ))) {
      StringBuilder sb = new StringBuilder();
      sb.append('\n');

      sb.append(run);
      sb.append(',');

      sb.append(trial);
      sb.append(',');

      sb.append(crossOver);
      sb.append(',');

      sb.append(mutation);
      sb.append(',');

      sb.append(popSize);
      sb.append(',');

      sb.append(evluations);
      sb.append(',');

      sb.append(time);
      sb.append(',');

      sb.append(solution);

      String s = sb.toString();
      System.out.println(s);
      pw.write(s);
      myMAp.put(s,solution);
    } catch (FileNotFoundException e) {
      System.out.println(e.getMessage());
    }

  }

  private static void writeToCSVHeader() {

    try (PrintWriter pw = new PrintWriter(new FileOutputStream(new File("experimentRuns.csv"), true ))) {
      StringBuilder sb = new StringBuilder();
      sb.append("Run id");
      sb.append(',');

      sb.append("Trial");
      sb.append(',');

      sb.append("CrossOver");
      sb.append(',');

      sb.append("Mutation");
      sb.append(',');

      sb.append("Population Size");
      sb.append(',');

      sb.append("Evaluation");
      sb.append(',');

      sb.append("Time");
      sb.append(',');

      sb.append("Solution");
      String s = sb.toString();
      System.out.println(s);
      pw.write(s);

    } catch (FileNotFoundException e) {
      System.out.println(e.getMessage());
    }

  }
}






