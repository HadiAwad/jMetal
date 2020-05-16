package org.uma.jmetal.bzu.algorithms;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.bzu.utils.AlgorithmTools;
import org.uma.jmetal.bzu.utils.IntRange;
import org.uma.jmetal.problem.PermutationProblem;
import org.uma.jmetal.solution.PermutationSolution;
import org.uma.jmetal.util.solutionattribute.impl.GenericSolutionAttribute;

import java.util.*;

public class SalpSwarmAlgorithm implements Algorithm<PermutationSolution<Integer>> {

    private final List<IntRange> boundConstraints;

    protected int populationSize;

    protected int maxIterations;

    protected long numberOfEvaluations;

    protected double tolerance;

    private boolean maximize = true;

    protected double minError = Double.MAX_VALUE;

    protected int best[];

    private int iterations;

    private GenericSolutionAttribute<PermutationSolution<Integer>, Integer> position;

    private PermutationProblem<PermutationSolution<Integer>> problem;

    private PermutationSolution<Integer> bestIndividual = null;

    public SalpSwarmAlgorithm(int population, int maxIterations , boolean maximize , PermutationProblem<PermutationSolution<Integer>> problem,List<IntRange> boundConstraints ){
        this.populationSize = population;
        this.maxIterations = maxIterations;
        this.maximize  = maximize;
        this.problem = problem;
        best = new int[problem.getPermutationLength()];
        this.boundConstraints = boundConstraints;
    }

    @Override
    public void run() {
        Random rand = new Random();

        //Reset variables
        minError = Double.MAX_VALUE;
        numberOfEvaluations = 0;

        //Create the population
        List<PermutationSolution<Integer>>  population = createInitialPopulation();
        sort(population);

        int index = 0;
        if(maximize){
            index =   population.size()-1;
        }

        minError = population.get(index).getObjective(0); // fitness
        best= Arrays.copyOf(getLocation(population.get(index)), boundConstraints.size());
        bestIndividual = population.get(index);
        Collections.shuffle(population);

        //Main algorithm
        for (int g = 0; g < maxIterations; g++) {

            double c1 = 2 * Math.exp(-Math.pow(4*(g+1)/ maxIterations,2));

            for (int i = 0; i < population.size(); i++) {

                if(i <= population.size() / 2){
                    for (int j = 0; j < boundConstraints.size(); j++) {
                        IntRange range = boundConstraints.get(j);
                        int newPosition;
                        if(rand.nextDouble() < 0.5) {
                            newPosition = (int) (best[j] + c1 * ((range.getMax() - range.getMin()) * rand.nextDouble() + range.getMin()));
                        }
                        else {
                            newPosition = (int) (best[j] - c1 * ((range.getMax() - range.getMin()) * rand.nextDouble() + range.getMin()));
                         //   (int) Math.ceil(
                            //TODO HAdi check here to convert to integer
                        }
                        population.get(i).setVariableValue(j,newPosition);
                    }
                }
                else if (i > (population.size()/2) && i < population.size()){
                    int[] salp1 = getLocation(population.get(i-1));
                    int[] salp2 =  getLocation(population.get(i));

                    for (int j = 0; j < salp2.length; j++){
                        salp2[j] = (salp2[j] + salp1[j]) / 2;
                    }
                }
            }

            //Clamp values
            for (int i = 0; i < population.size(); i++){
                AlgorithmTools.Clamp(getLocation(population.get(i)));
            }

            //Calculate fitness
            for (int i = 0; i < population.size(); i++) {
                problem.evaluate(population.get(i));
                double f = population.get(i).getObjective(0);
                if(f<0){
                    System.out.println("fitness is less than 0 "+f);
                }
                numberOfEvaluations++;
                if(f > minError){
                    minError = f;
                    best = Arrays.copyOf(getLocation(population.get(i)), boundConstraints.size());
                    bestIndividual = population.get(i);
                }
            }
        }
    }

    private void sort(List<PermutationSolution<Integer>> population) {
        Collections.sort(population, (o1, o2) -> {
            return  Double.compare( o1.getObjective(0)  , o2.getObjective(0));
        });
    }

    public List<PermutationSolution<Integer>> createInitialPopulation() {
        List<PermutationSolution<Integer>> swarm = new ArrayList<>(populationSize);

        PermutationSolution<Integer> newSolution;
        for (int i = 0; i < populationSize; i++) {
            newSolution = problem.createSolution();
            position.setAttribute(newSolution, i);
            swarm.add(newSolution);
        }

        return swarm;
    }

    @Override
    public PermutationSolution<Integer> getResult() {
        return this.bestIndividual;
    }

    @Override public String getName() {
        return "SalpSwarmAlgorithm" ;
    }

    @Override public String getDescription() {
        return "Salp Swarm Algorithm " ;
    }

    public int [] getLocation (PermutationSolution<Integer> individual){
        int []  location = new int[individual.getNumberOfVariables()];
        for (int i = 0; i < location.length ; i++) {
            location [i] = individual.getVariableValue(i);
        }
        return  location;
    }
}
