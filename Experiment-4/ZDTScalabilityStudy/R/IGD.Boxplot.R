postscript("IGD.eps", horizontal=FALSE, onefile=FALSE, height=8, width=12, pointsize=10)
resultDirectory<-"Experiment-4/ZDTScalabilityStudy/data"
qIndicator <- function(indicator, problem)
{
fileSMPSO<-paste(resultDirectory, "SMPSO", sep="/")
fileSMPSO<-paste(fileSMPSO, problem, sep="/")
fileSMPSO<-paste(fileSMPSO, indicator, sep="/")
SMPSO<-scan(fileSMPSO)

fileNSGAII<-paste(resultDirectory, "NSGAII", sep="/")
fileNSGAII<-paste(fileNSGAII, problem, sep="/")
fileNSGAII<-paste(fileNSGAII, indicator, sep="/")
NSGAII<-scan(fileNSGAII)

fileSPEA2<-paste(resultDirectory, "SPEA2", sep="/")
fileSPEA2<-paste(fileSPEA2, problem, sep="/")
fileSPEA2<-paste(fileSPEA2, indicator, sep="/")
SPEA2<-scan(fileSPEA2)

algs<-c("SMPSO","NSGAII","SPEA2")
boxplot(SMPSO,NSGAII,SPEA2,names=algs, notch = FALSE)
titulo <-paste(indicator, problem, sep=":")
title(main=titulo)
}
par(mfrow=c(3,3))
indicator<-"IGD"
qIndicator(indicator, "ZDT110")
qIndicator(indicator, "ZDT120")
qIndicator(indicator, "ZDT130")
qIndicator(indicator, "ZDT140")
qIndicator(indicator, "ZDT150")
