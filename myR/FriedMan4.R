# Title     : TODO
# Objective : TODO
# Created by: awad
# Created on: 4/28/2020

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


ibeaData = subset(Data, algorithm == 'IBEA')
nsgaData = subset(Data, algorithm == 'NSGAII')
modifiedData = subset(Data, algorithm == 'M_IBEA')

ibeaData %>% group_by(CUT) %>% summarise( MSize=median(Size), MLength=median(Length),MTotal_Goals=median(Total_Goals),MCovered_Goals=median(Covered_Goals),
                                          MMutaion=median(MutationScore), MCoverage=median(Coverage),
                                          MTotal_time=median(Total_Time), MBranchCoverage=median(BranchCoverage),
                                          MBZUTIME=median(BZUTestExecutionTime))%>%
  write.table( file = "myR/forPlot/IBEAMedian.csv", sep = ",", quote = FALSE, row.names = F)

nsgaData %>% group_by(CUT) %>% summarise( MSize=median(Size), MLength=median(Length),MTotal_Goals=median(Total_Goals),MCovered_Goals=median(Covered_Goals),
                                          MMutaion=median(MutationScore), MCoverage=median(Coverage),
                                          MTotal_time=median(Total_Time), MBranchCoverage=median(BranchCoverage),
                                          MBZUTIME=median(BZUTestExecutionTime))%>%
  write.table(file = "myR/forPlot/NSGAMedian.csv", sep = ",", quote = FALSE, row.names = F)

modifiedData %>% group_by(CUT) %>% summarise( MSize=median(Size), MLength=median(Length),MTotal_Goals=median(Total_Goals),MCovered_Goals=median(Covered_Goals),
                                              MMutaion=median(MutationScore), MCoverage=median(Coverage),
                                              MTotal_time=median(Total_Time), MBranchCoverage=median(BranchCoverage),
                                              MBZUTIME=median(BZUTestExecutionTime))%>%
  write.table( file = "myR/forPlot/ModifiedMeduan.csv", sep = ",", quote = FALSE, row.names = F)


