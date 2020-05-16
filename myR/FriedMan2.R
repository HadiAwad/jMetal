# Title     : Sample Friedman test
# Objective : Thesis statistical Analyss
# Created by: Hadi
# Created on: 4/14/2020

  require(psych)
  require(FSA)
  require(lattice)
  require(PMCMR)
  require(rcompanion)
  require(tidyverse)
  require(ggpubr)
  require(rstatix)
  library(tidyverse)
  library(ggpubr)
  library(rstatix)
  library(dplyr)



  Data <- read.csv(file = 'myR/mySampleData.csv')
  Data
  # Data$Algorithm = factor(Data$Algorithm, levels = unique(Data$Algorithm))
  # Data
  Data$MutationScore.factored = factor(Data$MutationScore, ordered = TRUE)
  Data$runID = factor(Data$runID,ordered = TRUE)
  Data
  headTail(Data)
  str(Data)
  summary(Data)

  histogram(~ Mutation | Algorithm,
          data=Data,
          layout=c(1,4)      #  columns and rows of individual plots
  )

  histogram(~ Mutation.factored | Algorithm,
            data=Data,
            layout=c(1,4)      #  columns and rows of individual plots
  )

  # get the mean
  Summarize(MutationScore ~ algorithm, data = Data, digits = 3)

  friedman.test(MutationScore ~ algorithm | CUT,, data = Data)

  # ordered according to mean we found
  Data$Algorithm = factor(Data$Algorithm, levels = c("mIBEA", "IBEA", "NSGAII"))

  ### Conover test
  # Adjusts p-values for multiple comparisons;
  PT = posthoc.friedman.conover.test(y = Data$Mutation.factored,
                                     groups = Data$Algorithm,
                                     blocks = Data$CUT,
                                     p.adjust.method = "fdr")
  PT
  ### Compact letter display
  PT0 = as.matrix(PT$p.value)
  PT1 = fullPTable(PT0)

  multcompLetters(PT1, compare = "<", threshold = 0.05, Letters = letters, reversed = FALSE)


# Second function
Data <- read.csv(file = 'myR/sampleData2.csv')
Data
head(Data, 3)

testtable <-Summarize(Length ~ algorithm, data = Data, digits = 3)
testtable
write.table(testtable, file = "myR/Length-summary.csv", sep = ",", quote = FALSE, row.names = F)

testtable <-Summarize(Size ~ algorithm, data = Data, digits = 3)
testtable
write.table(testtable, file = "myR/Size-summary.csv", sep = ",", quote = FALSE, row.names = F)

testtable <-Summarize(MutationScore ~ algorithm, data = Data, digits = 3)
testtable
write.table(testtable, file = "myR/MutationScore-summary.csv", sep = ",", quote = FALSE, row.names = F)

testtable <-Summarize(Total_Time ~ algorithm, data = Data, digits = 3)
testtable
write.table(testtable, file = "myR/Total_Time-summary.csv", sep = ",", quote = FALSE, row.names = F)

testtable <-Summarize(BranchCoverage ~ algorithm, data = Data, digits = 3)
testtable
write.table(testtable, file = "myR/BranchCoverage-summary.csv", sep = ",", quote = FALSE, row.names = F)

testtable <-Summarize(BZUTestExecutionTime ~ algorithm, data = Data, digits = 3)
testtable
write.table(testtable, file = "myR/BZUTestExecutionTime-summary.csv", sep = ",", quote = FALSE, row.names = F)

###########################################################################################
Data %>% group_by(algorithm) %>% get_summary_stats(MutationScore, type = "common")
#ggboxplot(Data, x = "algorithm", y = "MutationScore")

res.fried <- Data %>% friedman_test(MutationScore ~ algorithm |CUT)
res.fried

Data %>% friedman_effsize(MutationScore ~ Algorithm | CUT)
Data$Algorithm = factor(Data$Algorithm, levels = c("mIBEA", "IBEA", "NSGAII"))
pwc <- Data %>%
  wilcox_test(Mutation ~ Algorithm, paired = TRUE, p.adjust.method = "bonferroni")
pwc