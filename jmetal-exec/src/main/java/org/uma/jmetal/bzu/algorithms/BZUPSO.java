package org.uma.jmetal.bzu.algorithms;

import org.uma.jmetal.algorithm.impl.AbstractParticleSwarmOptimization;
import org.uma.jmetal.operator.Operator;
import org.uma.jmetal.operator.impl.selection.BestSolutionSelection;
import org.uma.jmetal.problem.PermutationProblem;
import org.uma.jmetal.solution.PermutationSolution;
import org.uma.jmetal.util.comparator.ObjectiveComparator;
import org.uma.jmetal.util.evaluator.SolutionListEvaluator;
import org.uma.jmetal.util.neighborhood.impl.AdaptiveRandomNeighborhood;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;
import org.uma.jmetal.util.solutionattribute.impl.GenericSolutionAttribute;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Class implementing a Standard PSO 2007 algorithm.
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */
@SuppressWarnings("serial")
public class BZUPSO extends AbstractParticleSwarmOptimization<PermutationSolution<Integer>, PermutationSolution<Integer>> {
    private PermutationProblem<PermutationSolution<Integer>> problem;
    private SolutionListEvaluator<PermutationSolution<Integer>> evaluator;

    private Operator<List<PermutationSolution<Integer>>, PermutationSolution<Integer>> findBestSolution;
    private Comparator<PermutationSolution<Integer>> fitnessComparator;
    private int swarmSize;
    private int maxIterations;
    private int iterations;
    private int numberOfParticlesToInform;
    private List<PermutationSolution<Integer>> localBest;
    private List<PermutationSolution<Integer>> neighborhoodBest;
    private double[][] speed;
    private AdaptiveRandomNeighborhood<PermutationSolution<Integer>> neighborhood;
    private GenericSolutionAttribute<PermutationSolution<Integer>, Integer> positionInSwarm;
    private double weight;
    private double c;
    private JMetalRandom randomGenerator = JMetalRandom.getInstance();
    private PermutationSolution<Integer> bestFoundParticle;
    private int numberOfTestCases;
    private int objectiveId;
    private int evaluations = 0;

    /**
     * Constructor
     *  @param problem
     * @param objectiveId This field indicates which objective, in the case of a multi-objective problem,
     *                    is selected to be optimized.
     * @param swarmSize
     * @param maxIterations
     * @param numberOfParticlesToInform
     * @param evaluator
     * @param numberOfTestCases
     */
    public BZUPSO(PermutationProblem<PermutationSolution<Integer>> problem, int objectiveId, int swarmSize, int maxIterations,
                  int numberOfParticlesToInform, SolutionListEvaluator<PermutationSolution<Integer>> evaluator, int numberOfTestCases) {
        this.problem= problem;
        this.swarmSize = swarmSize;
        this.maxIterations = maxIterations;
        this.numberOfParticlesToInform = numberOfParticlesToInform;
        this.evaluator = evaluator;
        this.objectiveId = objectiveId;
        this.numberOfTestCases=numberOfTestCases-1;

        weight = 1.0 / (2.0 * Math.log(2));
        c = 1.0 / 2.0 + Math.log(2);

        fitnessComparator = new ObjectiveComparator<PermutationSolution<Integer>>(objectiveId);
        findBestSolution = new BestSolutionSelection<PermutationSolution<Integer>>(fitnessComparator);

        localBest = new ArrayList<>();
        neighborhoodBest =  new ArrayList<>();
        speed = new double[swarmSize][problem.getNumberOfVariables()];

        positionInSwarm = new GenericSolutionAttribute<PermutationSolution<Integer>, Integer>();

        bestFoundParticle = null;
        neighborhood = new AdaptiveRandomNeighborhood<PermutationSolution<Integer>>(swarmSize, this.numberOfParticlesToInform);
    }

    /**
     * Constructor
     *
     * @param problem
     * @param swarmSize
     * @param maxIterations
     * @param numberOfParticlesToInform
     * @param evaluator
     */
    public BZUPSO(PermutationProblem<PermutationSolution<Integer>> problem, int swarmSize, int maxIterations,
                  int numberOfParticlesToInform, SolutionListEvaluator<PermutationSolution<Integer>> evaluator, int numberOfTestCases) {
        this(problem, 0, swarmSize, maxIterations, numberOfParticlesToInform, evaluator,numberOfTestCases);
    }

    @Override
    public void initProgress() {
        iterations = 1;
    }

    @Override
    public void updateProgress() {
        iterations += 1;
    }

    @Override
    public boolean isStoppingConditionReached() {
        return iterations >= maxIterations;
    }

    @Override
    public List<PermutationSolution<Integer>> createInitialSwarm() {
        List<PermutationSolution<Integer>> swarm = new ArrayList<>(swarmSize);

        PermutationSolution<Integer> newSolution;
        for (int i = 0; i < swarmSize; i++) {
            newSolution = problem.createSolution();
            positionInSwarm.setAttribute(newSolution, i);
            swarm.add(newSolution);
        }

        return swarm;
    }

    @Override
    public List<PermutationSolution<Integer>> evaluateSwarm(List<PermutationSolution<Integer>> swarm) {
        swarm = evaluator.evaluate(swarm, problem);
        evaluations++;
        return swarm;
    }

    @Override
    public void initializeLeader(List<PermutationSolution<Integer>> swarm) {
        for (int i = 0; i < swarm.size(); i++) {
            neighborhoodBest.add(i,getNeighborBest(i));
        }
    }

    @Override
    public void initializeParticlesMemory(List<PermutationSolution<Integer>> swarm) {
        for (int i = 0; i < swarm.size(); i++) {
            localBest.add(i, (PermutationSolution<Integer>) swarm.get(i).copy());
        }
    }

    @Override
    public void initializeVelocity(List<PermutationSolution<Integer>> swarm) {
        for (int i = 0; i < swarm.size(); i++) {
            PermutationSolution<Integer> particle = swarm.get(i);
            for (int j = 0; j < problem.getNumberOfVariables(); j++) {
                speed[i][j] =
                        (randomGenerator.nextDouble(0, numberOfTestCases)
                                - particle.getVariableValue(j)) / 2.0;
            }
        }
    }

    @Override
    public void updateVelocity(List<PermutationSolution<Integer>> swarm) {
        double r1, r2;

        for (int i = 0; i < swarmSize; i++) {
            PermutationSolution<Integer> particle = swarm.get(i);

            r1 = randomGenerator.nextDouble(0, c);
            r2 = randomGenerator.nextDouble(0, c);

            if (localBest.get(i) != neighborhoodBest.get(i)) {
                for (int var = 0; var < particle.getNumberOfVariables(); var++) {
                    speed[i][var] = weight * speed[i][var] +
                            r1 * (localBest.get(i).getVariableValue(var) - particle.getVariableValue(var)) +
                            r2 * (neighborhoodBest.get(i).getVariableValue(var) - particle.getVariableValue
                                    (var));
                }
            } else {
                for (int var = 0; var < particle.getNumberOfVariables(); var++) {
                    speed[i][var] = weight * speed[i][var] +
                            r1 * (localBest.get(i).getVariableValue(var) -
                                    particle.getVariableValue(var));
                }
            }
        }
    }

    @Override
    public void updatePosition(List<PermutationSolution<Integer>> swarm) {
        for (int i = 0; i < swarmSize; i++) {
            PermutationSolution<Integer> particle = swarm.get(i);
            for (int var = 0; var < particle.getNumberOfVariables(); var++) {
                particle.setVariableValue(var, (int) Math.round(particle.getVariableValue(var) + speed[i][var]));

                if (particle.getVariableValue(var) < 0) {
                    particle.setVariableValue(var, 0);
                    speed[i][var] = 0;
                }
                if (particle.getVariableValue(var) > numberOfTestCases) {
                    particle.setVariableValue(var, numberOfTestCases);
                    speed[i][var] = 0;
                }
            }
        }
    }

    @Override
    public void perturbation(List<PermutationSolution<Integer>> swarm) {
    /*
    MutationOperator<PermutationSolution<Integer>> mutation =
            new PolynomialMutation(1.0/problem.getNumberOfVariables(), 20.0) ;
    for (PermutationSolution<Integer> particle : swarm) {
      mutation.execute(particle) ;
    }
    */
    }

    @Override
    public void updateLeaders(List<PermutationSolution<Integer>> swarm) {
        for (int i = 0; i < swarm.size(); i++) {
            neighborhoodBest.add(i ,getNeighborBest(i) );
        }

        PermutationSolution<Integer> bestSolution = findBestSolution.execute(swarm);

        if (bestFoundParticle == null) {
            bestFoundParticle = bestSolution;
        } else {
            if (bestSolution.getObjective(objectiveId) == bestFoundParticle.getObjective(0)) {
                neighborhood.recompute();
            }
            if (bestSolution.getObjective(objectiveId) < bestFoundParticle.getObjective(0)) {
                bestFoundParticle = bestSolution;
            }
        }
    }

    @Override
    public void updateParticlesMemory(List<PermutationSolution<Integer>> swarm) {
        for (int i = 0; i < swarm.size(); i++) {
            if ((swarm.get(i).getObjective(objectiveId) < localBest.get(i).getObjective(0))) {
                localBest.add(i, (PermutationSolution<Integer>) swarm.get(i).copy());
            }
        }
    }

    @Override
    public PermutationSolution<Integer> getResult() {
        return bestFoundParticle;
    }

    private PermutationSolution<Integer>  getNeighborBest(int i) {
        PermutationSolution<Integer>  bestLocalBestSolution = null;

        for (PermutationSolution<Integer> solution : neighborhood.getNeighbors(getSwarm(), i)) {
            int solutionPositionInSwarm = positionInSwarm.getAttribute(solution);
            if ((bestLocalBestSolution == null) || (bestLocalBestSolution.getObjective(0)
                    > localBest.get(solutionPositionInSwarm).getObjective(0))) {
                bestLocalBestSolution = localBest.get(solutionPositionInSwarm);
            }
        }

        return bestLocalBestSolution ;
    }

    /* Getters */
    public double[][]getSwarmSpeedMatrix() {
        return speed ;
    }

    public List<PermutationSolution<Integer>> getLocalBest() {
        return localBest ;
    }

    @Override public String getName() {
        return "SPSO07" ;
    }

    @Override public String getDescription() {
        return "Standard PSO 2007" ;
    }

    public int getNumberOfEvaluations(){
        return evaluations;
    }
}
