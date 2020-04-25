package org.uma.jmetal.bzu;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.singleobjective.geneticalgorithm.GenerationalGeneticAlgorithm;
import org.uma.jmetal.algorithm.singleobjective.geneticalgorithm.GeneticAlgorithmBuilder;
import org.uma.jmetal.algorithm.singleobjective.geneticalgorithm.SteadyStateGeneticAlgorithm;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.crossover.PMXCrossover;
import org.uma.jmetal.operator.impl.crossover.SinglePointCrossover;
import org.uma.jmetal.operator.impl.mutation.BitFlipMutation;
import org.uma.jmetal.operator.impl.mutation.PermutationSwapMutation;
import org.uma.jmetal.problem.BinaryProblem;
import org.uma.jmetal.problem.PermutationProblem;
import org.uma.jmetal.solution.BinarySolution;
import org.uma.jmetal.solution.PermutationSolution;
import org.uma.jmetal.util.AlgorithmRunner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utility {

    public  static Map<String,Double> myMAp = new HashMap<>();

    public  static void runAlgorithmAss2(Algorithm<PermutationSolution<Integer>> algorithm, int numberOfRuns, int runId,
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

    public  static void runAlgorithmAss3(Algorithm<BinarySolution> algorithm, int numberOfRuns, int runId,
                                         double cr, double mutaion, int popSize, GeneticAlgorithmBuilder.GeneticAlgorithmVariant variant) {
        int runs = 0;
        for (runs = 0; runs < numberOfRuns; runs++) {

            AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm)
                    .execute();

            BinarySolution solution = algorithm.getResult();

            long computingTime = algorithmRunner.getComputingTime();

            int evaluations =0;
            if(GeneticAlgorithmBuilder.GeneticAlgorithmVariant.GENERATIONAL.equals(variant)){
                evaluations =  ((GenerationalGeneticAlgorithm) algorithm).getNumberOfEvaluations();
            }else{
                evaluations =  ((SteadyStateGeneticAlgorithm) algorithm).getNumberOfEvaluations();
            }

            String solutionStr=  solution.getVariableValueString(0);

            long count = solutionStr.chars().filter(ch -> ch == '1').count();

            writeToCSV(runId,runs,cr,mutaion,popSize,evaluations,computingTime,count);
        }

    }

    public static List<Algorithm<PermutationSolution<Integer>>>
    buildAssignment3Experiments(BinaryProblem problem, GeneticAlgorithmBuilder.GeneticAlgorithmVariant variant, double[] crArray, int[] popArray, double[] mArray, int numberOfRuns,
                                SelectionOperator<List<BinarySolution>, BinarySolution> selection) throws Exception {

        Algorithm<BinarySolution> algorithm;
        CrossoverOperator<BinarySolution> crossover;
        MutationOperator<BinarySolution> mutation;

        List<Algorithm<PermutationSolution<Integer>>> algorithmList = new ArrayList<>();
        int runId = 0;

        for (double cr : crArray) {
            crossover = new SinglePointCrossover(cr);
            for (double mutaion : mArray) {
                mutation = new BitFlipMutation(mutaion);
                for (int popSize : popArray) {
                    System.out.println(runId++ + "--" + cr + "--" + popSize + "--" + mutaion);
                    algorithm = new GeneticAlgorithmBuilder<>(problem, crossover, mutation)
                            .setPopulationSize(popSize)
                            .setMaxEvaluations(50000)
                            .setSelectionOperator(selection)
                            .setVariant(variant)
                            .build();
                    runAlgorithmAss3(algorithm,numberOfRuns,runId,cr,mutaion,popSize,variant);
                }
            }
        }


        return algorithmList;
    }

    public static List<Algorithm<PermutationSolution<Integer>>>
    buildAssginment2Experiments(PermutationProblem<PermutationSolution<Integer>>
                                        problem, double[] crArray, int[] popArray, double[] mArray, int numberOfRuns,
                                SelectionOperator<List<PermutationSolution<Integer>>, PermutationSolution<Integer>> selection) throws Exception {


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
                            .setMaxEvaluations(200000)
                            .setSelectionOperator(selection)
                            .build();
                    runAlgorithmAss2(algorithm,numberOfRuns,runId,cr,mutaion,popSize);
                }
            }
        }


        return algorithmList;
    }

    public static void writeToCSV(int run, int trial, double crossOver, double mutation, int popSize, long evluations,
                                  long time, double solution) {

        try (PrintWriter pw = new PrintWriter(new FileOutputStream(new File("experimentRuns3_1.csv"), true ))) {
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

    public static void writeToCSVHeader() {
        myMAp.clear();
        try (PrintWriter pw = new PrintWriter(new FileOutputStream(new File("experimentRuns3_1.csv"), true ))) {
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
