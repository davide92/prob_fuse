# ProbFuse #

### INFO ###

This code was implemented during an Information Retrieval course by:

Luca Rossi;
Davide Storato;
Roberto Tarantini;
Andrea Ziggiotto.

### COMB METHODS ###

The class ComputeRankFusion is used to execute the base methods, the parameters PathInput and PathOutput must be set in this way:  
**-PathInput**: is the directory where are saved all the original runs;  
**-PathOutput**: is the directory where the fusion run will be saved.    
This class computes all Comb methods at once, the output is compound of 6 different txt files in TREC format.    

### PROBFUSE ###

The class ComputeProbFuse is used to execute ProbFuse, the parameters pathRun, pathQrels, pathOutput must be set in this way:    
**-pathRun:** is the directory where are saved all the original run;  
**-pathQrels:** is the position where is saved the file of the qrels;   
**-pathOutput:** is the directory where will be saved the txt file of the final run.   
To executes this software, it must be set also the dimension of the training size and the number of segments. These value can be set in the call 
of the ProbFuse class constructor.
