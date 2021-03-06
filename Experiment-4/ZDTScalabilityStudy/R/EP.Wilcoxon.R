write("", "Experiment-4/ZDTScalabilityStudy/R/EP.Wilcoxon.tex",append=FALSE)
resultDirectory<-"Experiment-4/ZDTScalabilityStudy/data"
latexHeader <- function() {
  write("\\documentclass{article}", "Experiment-4/ZDTScalabilityStudy/R/EP.Wilcoxon.tex", append=TRUE)
  write("\\title{StandardStudy}", "Experiment-4/ZDTScalabilityStudy/R/EP.Wilcoxon.tex", append=TRUE)
  write("\\usepackage{amssymb}", "Experiment-4/ZDTScalabilityStudy/R/EP.Wilcoxon.tex", append=TRUE)
  write("\\author{A.J.Nebro}", "Experiment-4/ZDTScalabilityStudy/R/EP.Wilcoxon.tex", append=TRUE)
  write("\\begin{document}", "Experiment-4/ZDTScalabilityStudy/R/EP.Wilcoxon.tex", append=TRUE)
  write("\\maketitle", "Experiment-4/ZDTScalabilityStudy/R/EP.Wilcoxon.tex", append=TRUE)
  write("\\section{Tables}", "Experiment-4/ZDTScalabilityStudy/R/EP.Wilcoxon.tex", append=TRUE)
  write("\\", "Experiment-4/ZDTScalabilityStudy/R/EP.Wilcoxon.tex", append=TRUE)
}

latexTableHeader <- function(problem, tabularString, latexTableFirstLine) {
  write("\\begin{table}", "Experiment-4/ZDTScalabilityStudy/R/EP.Wilcoxon.tex", append=TRUE)
  write("\\caption{", "Experiment-4/ZDTScalabilityStudy/R/EP.Wilcoxon.tex", append=TRUE)
  write(problem, "Experiment-4/ZDTScalabilityStudy/R/EP.Wilcoxon.tex", append=TRUE)
  write(".EP.}", "Experiment-4/ZDTScalabilityStudy/R/EP.Wilcoxon.tex", append=TRUE)

  write("\\label{Table:", "Experiment-4/ZDTScalabilityStudy/R/EP.Wilcoxon.tex", append=TRUE)
  write(problem, "Experiment-4/ZDTScalabilityStudy/R/EP.Wilcoxon.tex", append=TRUE)
  write(".EP.}", "Experiment-4/ZDTScalabilityStudy/R/EP.Wilcoxon.tex", append=TRUE)

  write("\\centering", "Experiment-4/ZDTScalabilityStudy/R/EP.Wilcoxon.tex", append=TRUE)
  write("\\begin{scriptsize}", "Experiment-4/ZDTScalabilityStudy/R/EP.Wilcoxon.tex", append=TRUE)
  write("\\begin{tabular}{", "Experiment-4/ZDTScalabilityStudy/R/EP.Wilcoxon.tex", append=TRUE)
  write(tabularString, "Experiment-4/ZDTScalabilityStudy/R/EP.Wilcoxon.tex", append=TRUE)
  write("}", "Experiment-4/ZDTScalabilityStudy/R/EP.Wilcoxon.tex", append=TRUE)
  write(latexTableFirstLine, "Experiment-4/ZDTScalabilityStudy/R/EP.Wilcoxon.tex", append=TRUE)
  write("\\hline ", "Experiment-4/ZDTScalabilityStudy/R/EP.Wilcoxon.tex", append=TRUE)
}

printTableLine <- function(indicator, algorithm1, algorithm2, i, j, problem) { 
  file1<-paste(resultDirectory, algorithm1, sep="/")
  file1<-paste(file1, problem, sep="/")
  file1<-paste(file1, indicator, sep="/")
  data1<-scan(file1)
  file2<-paste(resultDirectory, algorithm2, sep="/")
  file2<-paste(file2, problem, sep="/")
  file2<-paste(file2, indicator, sep="/")
  data2<-scan(file2)
  if (i == j) {
    write("-- ", "Experiment-4/ZDTScalabilityStudy/R/EP.Wilcoxon.tex", append=TRUE)
  }
  else if (i < j) {
    if (is.finite(wilcox.test(data1, data2)$p.value) & wilcox.test(data1, data2)$p.value <= 0.05) {
      if (median(data1) <= median(data2)) {
        write("$\\blacktriangle$", "Experiment-4/ZDTScalabilityStudy/R/EP.Wilcoxon.tex", append=TRUE)
}
      else {
        write("$\\triangledown$", "Experiment-4/ZDTScalabilityStudy/R/EP.Wilcoxon.tex", append=TRUE)
}
    }
    else {
      write("--", "Experiment-4/ZDTScalabilityStudy/R/EP.Wilcoxon.tex", append=TRUE)
    }
  }
  else {
    write(" ", "Experiment-4/ZDTScalabilityStudy/R/EP.Wilcoxon.tex", append=TRUE)
  }
}

latexTableTail <- function() { 
  write("\\hline", "Experiment-4/ZDTScalabilityStudy/R/EP.Wilcoxon.tex", append=TRUE)
  write("\\end{tabular}", "Experiment-4/ZDTScalabilityStudy/R/EP.Wilcoxon.tex", append=TRUE)
  write("\\end{scriptsize}", "Experiment-4/ZDTScalabilityStudy/R/EP.Wilcoxon.tex", append=TRUE)
  write("\\end{table}", "Experiment-4/ZDTScalabilityStudy/R/EP.Wilcoxon.tex", append=TRUE)
}

latexTail <- function() { 
  write("\\end{document}", "Experiment-4/ZDTScalabilityStudy/R/EP.Wilcoxon.tex", append=TRUE)
}

### START OF SCRIPT 
# Constants
problemList <-c("ZDT110", "ZDT120", "ZDT130", "ZDT140", "ZDT150") 
algorithmList <-c("SMPSO", "NSGAII", "SPEA2") 
tabularString <-c("lcc") 
latexTableFirstLine <-c("\\hline  & NSGAII & SPEA2\\\\ ") 
indicator<-"EP"

 # Step 1.  Writes the latex header
latexHeader()
tabularString <-c("| l | p{0.15cm }p{0.15cm }p{0.15cm }p{0.15cm }p{0.15cm } | p{0.15cm }p{0.15cm }p{0.15cm }p{0.15cm }p{0.15cm } | ") 

latexTableFirstLine <-c("\\hline \\multicolumn{1}{|c|}{} & \\multicolumn{5}{c|}{NSGAII} & \\multicolumn{5}{c|}{SPEA2} \\\\") 

# Step 3. Problem loop 
latexTableHeader("ZDT110 ZDT120 ZDT130 ZDT140 ZDT150 ", tabularString, latexTableFirstLine)

indx = 0
for (i in algorithmList) {
  if (i != "SPEA2"){
)    write(i , "Experiment-4/ZDTScalabilityStudy/R/EP.Wilcoxon.tex", append=TRUE)
    write(" & ", "Experiment-4/ZDTScalabilityStudy/R/EP.Wilcoxon.tex", append=TRUE)

    jndx = 0
    for (j in algorithmList) {
      for (problem in problemList) {
        if (jndx != 0) {
          if (i != j) {
            printTableLine(indicator, i, j, indx, jndx, problem)
          }
          else {
            write("  ", "Experiment-4/ZDTScalabilityStudy/R/EP.Wilcoxon.tex", append=TRUE)
          } 
          if (problem == "ZDT150"){
)            if (j == "SPEA2"){
)              write(" \\\\ ", "Experiment-4/ZDTScalabilityStudy/R/EP.Wilcoxon.tex", append=TRUE)
            } 
            else {
              write(" & ", "Experiment-4/ZDTScalabilityStudy/R/EP.Wilcoxon.tex", append=TRUE)
            }
          }
     else {
    write("&", "Experiment-4/ZDTScalabilityStudy/R/EP.Wilcoxon.tex", append=TRUE)
     }
        }
      }
      jndx = jndx + 1
}
    indx = indx + 1
  }
} # for algorithm

  latexTableTail()

#Step 3. Writes the end of latex file 
latexTail()

