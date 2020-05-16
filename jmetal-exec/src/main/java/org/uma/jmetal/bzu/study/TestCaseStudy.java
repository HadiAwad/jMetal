package org.uma.jmetal.bzu.study;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.ibea.IBEABuilder;
import org.uma.jmetal.algorithm.multiobjective.mocell.MOCellBuilder;
import org.uma.jmetal.algorithm.multiobjective.nsgaii.NSGAIIBuilder;
import org.uma.jmetal.bzu.TestCasePrioritization;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.crossover.PMXCrossover;
import org.uma.jmetal.operator.impl.mutation.PermutationSwapMutation;
import org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection;
import org.uma.jmetal.qualityindicator.impl.Spread;
import org.uma.jmetal.qualityindicator.impl.hypervolume.PISAHypervolume;
import org.uma.jmetal.solution.PermutationSolution;
import org.uma.jmetal.util.comparator.RankingAndCrowdingDistanceComparator;
import org.uma.jmetal.util.experiment.Experiment;
import org.uma.jmetal.util.experiment.ExperimentBuilder;
import org.uma.jmetal.util.experiment.component.*;
import org.uma.jmetal.util.experiment.util.ExperimentAlgorithm;
import org.uma.jmetal.util.experiment.util.ExperimentProblem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestCaseStudy {
    public static String MAIN_PATH = "C:\\Users\\awad\\Downloads\\For Hadi\\SBSE\\jMetal-master\\jmetal-core\\src\\main\\resources\\bzudata\\";
    public static  int INDEPENDENT_RUNS = 20;

    public static void main(String[] args) throws IOException {
        String experimentBaseDirectory = "RealExperiment";
        boolean costUnified  = false;
        if(args.length > 0){
            if(args.length == 0){
                if(System.getProperty("os.name").startsWith("Windows")){
                    MAIN_PATH = "C:\\Users\\awad\\Downloads\\For Hadi\\SBSE\\jMetal-master\\jmetal-core\\src\\main\\resources\\bzudata\\";
                }else{
                    MAIN_PATH = "/home/awadhadi/tcp/jMetal-master/jmetal-core/src/main/resources/bzudata/";
                }
            }else{
                MAIN_PATH = args[0];
                if(args.length == 2){
                    INDEPENDENT_RUNS = Integer.valueOf(args[1]);
                }
            }

            if(args.length == 3){
                experimentBaseDirectory = args[2];
            }

            if(args.length == 4){
                costUnified = Boolean.valueOf(args[3]);
            }
        }

        List<ExperimentProblem<PermutationSolution<Integer>>> problemList = new ArrayList<>();
        problemList.add(new ExperimentProblem<>(new TestCasePrioritization(MAIN_PATH+"bash",costUnified), "bash"));
        problemList.add(new ExperimentProblem<>(new TestCasePrioritization( MAIN_PATH+"flex",costUnified), "flex"));
        problemList.add(new ExperimentProblem<>(new TestCasePrioritization( MAIN_PATH+"grep",costUnified), "grep"));
        problemList.add(new ExperimentProblem<>(new TestCasePrioritization(MAIN_PATH+"printtokens",costUnified), "printtokens"));
        problemList.add(new ExperimentProblem<>(new TestCasePrioritization( MAIN_PATH+"printtokens2",costUnified), "printtokens2"));
        problemList.add(new ExperimentProblem<>(new TestCasePrioritization(MAIN_PATH+"sed",costUnified), "sed"));

        List<ExperimentAlgorithm<PermutationSolution<Integer>, List<PermutationSolution<Integer>>>> algorithmList
                = configureAlgorithmList(problemList);

        Experiment<PermutationSolution<Integer>, List<PermutationSolution<Integer>>> experiment =
                new ExperimentBuilder<PermutationSolution<Integer>, List<PermutationSolution<Integer>>>("TCP")
                        .setAlgorithmList(algorithmList)
                        .setProblemList(problemList)
                        .setExperimentBaseDirectory(experimentBaseDirectory)
                        .setOutputParetoFrontFileName("FUN")
                        .setOutputParetoSetFileName("VAR")
                        .setReferenceFrontDirectory("/pareto_fronts")
                        .setIndicatorList(Arrays.asList(
                                new Spread<>(),
                                new PISAHypervolume<>()))
                        .setIndependentRuns(INDEPENDENT_RUNS)
                        .setNumberOfCores(4)
                        .build();

     new ExecuteAlgorithms<>(experiment).run();
///       new GenerateReferenceParetoSetAndFrontFromDoubleSolutions(experiment).run();
        new ComputeQualityIndicators<>(experiment).run();
        new GenerateLatexTablesWithStatistics(experiment).run();
        new GenerateWilcoxonTestTablesWithR<>(experiment).run();
        new GenerateFriedmanTestTables<>(experiment).run();
        new GenerateBoxplotsWithR<>(experiment).setRows(3).setColumns(3).run();
    }

    static List<ExperimentAlgorithm<PermutationSolution<Integer>, List<PermutationSolution<Integer>>>> configureAlgorithmList(
            List<ExperimentProblem<PermutationSolution<Integer>>> problemList) {

        List< ExperimentAlgorithm<PermutationSolution<Integer>, List<PermutationSolution<Integer>>>> algorithms = new ArrayList<>();

        int populationSize_ = 256;
        int maxEvaluations_ = 25000;
        double mutationProbability_ = 1.0 / problemList.get(0).getProblem().getNumberOfVariables();
        double crossoverProbability_ = 0.9;

        CrossoverOperator<PermutationSolution<Integer>>  crossover = new PMXCrossover(crossoverProbability_);
        MutationOperator<PermutationSolution<Integer>> mutation  = new PermutationSwapMutation<Integer>(mutationProbability_);
        SelectionOperator<List<PermutationSolution<Integer>>, PermutationSolution<Integer>> selection =
                new BinaryTournamentSelection<PermutationSolution<Integer>>(new RankingAndCrowdingDistanceComparator<PermutationSolution<Integer>>());

        for (int run = 0; run < INDEPENDENT_RUNS; run++) {

            for (int i = 0; i < problemList.size(); i++) {
                Algorithm<List<PermutationSolution<Integer>>> algorithm= new NSGAIIBuilder<PermutationSolution<Integer>>(
                        problemList.get(i).getProblem(),
                        crossover,
                        mutation)
                        .setSelectionOperator(selection).
                         setMaxEvaluations(maxEvaluations_)
                        .setPopulationSize(populationSize_)
                        .build();
                algorithms.add(new ExperimentAlgorithm<>(algorithm, problemList.get(i), run));
            }

            for (int i = 0; i < problemList.size(); i++) {
                Algorithm<List<PermutationSolution<Integer>>> algorithm = new MOCellBuilder<PermutationSolution<Integer>>(
                        problemList.get(i).getProblem(),
                        crossover,
                        mutation)
                        .setSelectionOperator(selection)
                        .setMaxEvaluations(maxEvaluations_)
                        .setPopulationSize(populationSize_)
                        .build();
                algorithms.add(new ExperimentAlgorithm<>(algorithm, problemList.get(i), run));
            }

            for (int i = 0; i < problemList.size(); i++) {
                Algorithm<List<PermutationSolution<Integer>>> algorithm = new IBEABuilder<PermutationSolution<Integer>>(
                        problemList.get(i).getProblem(),
                        crossover,
                        mutation)
                        .setSelectionOperator(selection)
                        .setMaxEvaluations(maxEvaluations_)
                        .setPopulationSize(populationSize_)
                        .build();
               algorithms.add(new ExperimentAlgorithm<>(algorithm, problemList.get(i), run));
            }
        }
        return algorithms;
    }
}
