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
# Second function
Data <- read.csv(file = 'myR/statistics.csv')
Data
head(Data, 3)


###

ibeaData = subset(Data, algorithm == 'IBEA')
nsgaData = subset(Data, algorithm == 'NSGAII')
modifiedData = subset(Data, algorithm == 'M_IBEA')

ibeaData %>% group_by(CUT) %>% summarise( MSize=median(Size), MLength=median(Length),
                                              MMutaion=median(MutationScore), MCoverage=median(Coverage),
                                              MTotal_time=median(Total_Time), MBranchCoverage=median(BranchCoverage),
                                              MBZUTIME=median(BZUTestExecutionTime))%>%
  write.table( file = "myR/IBEAMedian.csv", sep = ",", quote = FALSE, row.names = F)

nsgaData %>% group_by(CUT) %>% summarise( MSize=median(Size), MLength=median(Length),
                                              MMutaion=median(MutationScore), MCoverage=median(Coverage),
                                              MTotal_time=median(Total_Time), MBranchCoverage=median(BranchCoverage),
                                              MBZUTIME=median(BZUTestExecutionTime))%>%
write.table(file = "myR/NSGAMedian.csv", sep = ",", quote = FALSE, row.names = F)

modifiedData %>% group_by(CUT) %>% summarise( MSize=median(Size), MLength=median(Length),
                                              MMutaion=median(MutationScore), MCoverage=median(Coverage),
                                              MTotal_time=median(Total_Time), MBranchCoverage=median(BranchCoverage),
                                              MBZUTIME=median(BZUTestExecutionTime))%>%
write.table( file = "myR/ModifiedMeduan.csv", sep = ",", quote = FALSE, row.names = F)

Data <- read.csv(file = 'myR/medians.csv')
head(Data, 3)
#ggboxplot(Data, x = "algorithm", y = "MMutaion")

res.fried <- Data %>% friedman_test(MMutaion ~ algorithm |CUT)
write.table(res.fried, file = "myR/tests/MMutaion_friedman_test.csv", sep = ",", quote = FALSE, row.names = F)

#Data %>% friedman_effsize(MLength ~ algorithm | CUT)
Data$algorithm = factor(Data$algorithm, levels = c("M_IBEA", "IBEA", "NSGAII"))
pwc <- Data %>% wilcox_test(MMutaion ~ algorithm, paired = TRUE, p.adjust.method = "bonferroni")
pwc
write.table(pwc, file = "myR/tests/MMutaion_pairwise.csv", sep = ",", quote = FALSE, row.names = F)