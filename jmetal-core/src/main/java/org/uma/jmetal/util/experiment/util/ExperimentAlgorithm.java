package org.uma.jmetal.util.experiment.util;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.experiment.Experiment;
import org.uma.jmetal.util.fileoutput.SolutionListOutput;
import org.uma.jmetal.util.fileoutput.impl.DefaultFileOutputContext;

import java.io.*;
import java.util.List;

/**
 * Class defining tasks for the execution of algorithms in parallel.
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */
public class ExperimentAlgorithm<S extends Solution<?>, Result>  {
  private Algorithm<Result> algorithm;
  private String algorithmTag;
  private String problemTag;
  private String referenceParetoFront;
  private int runId ;

  /**
   * Constructor
   */
  public ExperimentAlgorithm(
          Algorithm<Result> algorithm,
          String algorithmTag,
          ExperimentProblem<S> problem,
          int runId) {
    this.algorithm = algorithm;
    this.algorithmTag = algorithmTag;
    this.problemTag = problem.getTag();
    this.referenceParetoFront = problem.getReferenceFront();
    this.runId = runId ;
  }

  public ExperimentAlgorithm(
          Algorithm<Result> algorithm,
          ExperimentProblem<S> problem,
          int runId) {

    this(algorithm,algorithm.getName(),problem,runId);

  }

  public void runAlgorithm(Experiment<?, ?> experimentData) {
    String outputDirectoryName = experimentData.getExperimentBaseDirectory()
            + "/data/"
            + algorithmTag
            + "/"
            + problemTag;

    File outputDirectory = new File(outputDirectoryName);
    if (!outputDirectory.exists()) {
      boolean result = new File(outputDirectoryName).mkdirs();
      if (result) {
        JMetalLogger.logger.info("Creating " + outputDirectoryName);
      } else {
        JMetalLogger.logger.severe("Creating " + outputDirectoryName + " failed");
      }
    }

    String funFile = outputDirectoryName + "/FUN" + runId + ".tsv";
    String varFile = outputDirectoryName + "/VAR" + runId + ".tsv";
    JMetalLogger.logger.info(
            " Running algorithm: " + algorithmTag +
                    ", problem: " + problemTag +
                    ", run: " + runId +
                    ", funFile: " + funFile);

    long initTime = System.currentTimeMillis();
    algorithm.run();
    long estimatedTime = System.currentTimeMillis() - initTime;

    JMetalLogger.logger.info(
            " Running algorithm: " + algorithmTag +
                    ", problem: " + problemTag +
                    ", run: " + runId +
                    ", funFile: " + funFile + "*** TIME : " +estimatedTime);
    Result population = algorithm.getResult();
    String timeFile =    experimentData.getExperimentBaseDirectory()
            + "/data/Time.csv";

    synchronized (this){
      writeToFile(timeFile,String.valueOf(estimatedTime));
      writeToFile(timeFile,String.valueOf(estimatedTime));
    }

    new SolutionListOutput((List<S>) population)
            .setSeparator("\t")
            .setVarFileOutputContext(new DefaultFileOutputContext(varFile))
            .setFunFileOutputContext(new DefaultFileOutputContext(funFile))
            .print();
  }

  public Algorithm<Result> getAlgorithm() {
    return algorithm;
  }

  public String getAlgorithmTag() {
    return algorithmTag;
  }

  public String getProblemTag() {
    return problemTag;
  }

  public String getReferenceParetoFront() { return referenceParetoFront; }

  public int getRunId() { return this.runId;}

  public void writeToFile (String fileNAme, String...values){
    File file = new File(fileNAme);
    if (! (file.exists() && file.isFile())) {
      try {
        file.createNewFile();
      } catch (IOException e) {
        e.printStackTrace();
        JMetalLogger.logger.severe("**Creating " + file + " failed");
      }
    }
    try {
      FileWriter fr = new FileWriter(file, true);
      BufferedWriter br = new BufferedWriter(fr);
      PrintWriter pr = new PrintWriter(br,true);
      String str = algorithmTag+","+runId+","+problemTag+","+values[0];
      for (int i = 1; i <values.length ; i++) {
        str+=","+values[i];
      }
      pr.println(str);
      pr.close();
      br.close();
      fr.close();
    } catch (IOException e) {
      JMetalLogger.logger.severe("**FileWriter " + file + " failed");
      e.printStackTrace();
    }
  }
}
