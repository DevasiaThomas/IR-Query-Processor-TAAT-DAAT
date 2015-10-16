# IR-Query-Processor-TAAT-DAAT
Query Processing Module for Boolean AND and OR operations using Term at a Time and Document at a Time strategies. 
Shows statistics about each method used.

Argument Inputs: IndexFile OutputFile N-ForTop-N-Terms QueryFile 

The format of the IndexFile(term.idx) is as follows 
"Term"\c"No of Documents present in"\m["DocID"/"Frequency in Doc", "DocID"/"Frequency in Doc",.,.,.,.,.,., "DocID"/"Frequency in Doc"]

Outputfile(logfile): See file 

N-For-Top-N-Terms: A positive integer.

Query File(sample_input) handles multiple queries; each query on a separate line. The program does Boolean And and Or Operations on each query(line : Each line contains 'n' query terms)


