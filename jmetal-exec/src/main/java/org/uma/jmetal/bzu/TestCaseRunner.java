package org.uma.jmetal.bzu;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.singleobjective.geneticalgorithm.GenerationalGeneticAlgorithm;
import org.uma.jmetal.algorithm.singleobjective.geneticalgorithm.GeneticAlgorithmBuilder;
import org.uma.jmetal.bzu.algorithms.PermutationPSO;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.crossover.PMXCrossover;
import org.uma.jmetal.operator.impl.mutation.PermutationSwapMutation;
import org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection;
import org.uma.jmetal.problem.PermutationProblem;
import org.uma.jmetal.solution.PermutationSolution;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.comparator.RankingAndCrowdingDistanceComparator;
import org.uma.jmetal.util.evaluator.impl.SequentialSolutionListEvaluator;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TestCaseRunner {

    public static void main(String[] args) throws Exception {
        int ga = 1;
        PermutationProblem<PermutationSolution<Integer>> problem;
        Algorithm<PermutationSolution<Integer>> algorithm;
        CrossoverOperator<PermutationSolution<Integer>> crossover;
        MutationOperator<PermutationSolution<Integer>> mutation;
        SelectionOperator<List<PermutationSolution<Integer>>, PermutationSolution<Integer>> selection;

        problem = new TestCasePrioritization_Single("C:\\Users\\awad\\Downloads\\For Hadi\\SBSE\\jMetal-master\\jmetal-core\\src\\main\\resources\\bzudata\\test");

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        crossover = new PMXCrossover(0.75);
        mutation = new PermutationSwapMutation<Integer>(0.50);
        selection = new BinaryTournamentSelection<PermutationSolution<Integer>>(new RankingAndCrowdingDistanceComparator<PermutationSolution<Integer>>());

        if(ga==1) {
            algorithm = new GeneticAlgorithmBuilder<>(problem, crossover, mutation)
                    .setPopulationSize(250)
                    .setMaxEvaluations(25000)
                    .setSelectionOperator(selection)
                    .setVariant(GeneticAlgorithmBuilder.GeneticAlgorithmVariant.GENERATIONAL)
                    .build();
        }else{
            SequentialSolutionListEvaluator<PermutationSolution<Integer>> evaluator = new SequentialSolutionListEvaluator<PermutationSolution<Integer>>();;
            algorithm = new PermutationPSO(problem,
                    10,
                    2500, 1, evaluator,problem.getNumberOfVariables()) ;
        }
        AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm)
                .execute();

        PermutationSolution<Integer> solution = algorithm.getResult();
        List<PermutationSolution<Integer>> population = new ArrayList<>(1);
        population.add(solution);
        int evaluaions = 0;
        long computingTime = algorithmRunner.getComputingTime();
        if(ga  == 1){
             evaluaions = ((GenerationalGeneticAlgorithm) algorithm).getNumberOfEvaluations();
        }else{
             evaluaions = ((PermutationPSO) algorithm).getNumberOfEvaluations();
        }

        JMetalLogger.logger.info("Total execution time: " + computingTime + " ms" + "evaluaions: " + evaluaions);
        JMetalLogger.logger.info("best solution  :" +solution.getObjective(0));
        JMetalLogger.logger.info("best solution  :" +solution);

        double a = solution.getObjective(0);
    }
}


