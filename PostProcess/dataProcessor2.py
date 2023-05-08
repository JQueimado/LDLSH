from fileinput import filename
import sys
import os
import pandas as pd

def main( testsFolder ):
    #setup context
    if ( not testsFolder.endswith("/") ):
        testsFolder = testsFolder + "/"
    
    tests = os.listdir(testsFolder)
    tests.sort()

    queryLatencydf = pd.DataFrame(columns=["test","mean", "meanplus", "meanminus"])
    queryAccuracydf = pd.DataFrame(columns=["test","mean"])
    queryThroughputdf = pd.DataFrame(columns=["test", "mean", "meanplus", "meanminus"])

    for test in tests:
        test = testsFolder + test
        #check dir
        if not os.path.isdir(test):
            continue

        #add directory extencion
        if ( not test.endswith("/") ):
            test = test + "/"

        #Latency
        currentdf = pd.read_csv( test + "latency.mean.csv", header=None )
        currentdf = currentdf.iloc[1:]
        testname = os.path.basename( test[:-1] ).split("_")[0]
        mean = currentdf[1].mean()
        std = currentdf[1].std()
        
        queryLatencydf.loc[len(queryLatencydf)] = [testname, mean, mean + std, mean - std]

        #Accuracy
        currentdf = pd.read_csv( test + "accuracy.mean.csv", header=None )
        currentdf = currentdf.iloc[1:]
        testname = os.path.basename( test[:-1] ).split("_")[0]
        mean = currentdf[1].mean()

        queryAccuracydf.loc[len(queryAccuracydf)] = [testname, mean]

        #Throughput
        currentdf = pd.read_csv( test + "throughput.stats.csv", header=None )
        testname = os.path.basename( test[:-1] ).split("_")[0]
        std = float( currentdf.iloc[4][2] )
        mean = float( currentdf.iloc[5][2] )

        queryThroughputdf.loc[len(queryAccuracydf)] = [testname, mean, mean+std, mean-std]

    print("Latency:\n",queryLatencydf)
    queryLatencydf.to_csv( testsFolder+"/TestLatencyAverage.csv")

    print("Accuracy:\n",queryAccuracydf)
    queryAccuracydf.to_csv( testsFolder+"/TestAccuracyAverage.csv" )

    print("Throughput:\n", queryThroughputdf)
    queryThroughputdf.to_csv( testsFolder+"/TestThroughputAverage.csv" )

    #Latency with threshold
    queryLatencyThresholddf = addThreshold(queryLatencydf)
    print("Latency with Threshold:\n",queryLatencyThresholddf)
    queryLatencyThresholddf.to_csv(testsFolder+"/ThresholdxLatency.csv", index=None, header=None, sep="\t")
    splitModels( testsFolder+"/ThresholdxLatency.csv" ) 

    #Accuracy with threshold
    queryAccuracyThresholddf = addThreshold( queryAccuracydf )
    print("Accuracy with Threshold\n", queryAccuracyThresholddf)
    queryAccuracyThresholddf.to_csv(testsFolder+"/ThresholdxAccuracy.csv", index=None, header=None, sep="\t")
    splitModels( testsFolder+"/ThresholdxAccuracy.csv" )

    #Througput with threshold
    queryThroughputThresholddf = addThreshold(queryThroughputdf)
    print("Threshold with througput\n", queryThroughputThresholddf)
    queryThroughputThresholddf.to_csv(testsFolder+"/ThresholdxThrughput.csv", index=None, header=None, sep="\t")
    splitModels( testsFolder+"/ThresholdxThrughput.csv" )

#Chages the Testnames to their respective Thresholds
def addThreshold( df: pd.DataFrame ) -> pd.DataFrame:

    for i, row in df.iterrows():
        
        threshold : float = 0.0
        test : str = row["test"]

        if( test.endswith("test-6") ):
            df.drop([i], axis=0, inplace=True)
            continue

        if( test.endswith("test-2") ):
            threshold = 0.75
        if( test.endswith("test-3") ):
            threshold = 0.90
        if( test.endswith("test-4") ):
            threshold = 0.95
        
        df.at[i,"threshold"] = threshold
    df.reset_index(drop=True, inplace=True)

    df.drop("test", axis=1, inplace=True)
    return df

#Splits models for the gnuplot interface
def splitModels( fname : str ):
    with open( fname, "r" ) as f:
        lines = f.readlines()

    for i in range(2, len(lines), 3):
        lines[i] = lines[i] + "\n\n"

    with open( fname, "w") as f:
        f.writelines( lines )

#__main__#
if __name__ == "__main__":
    argv = sys.argv
    if( len(argv) != 2 ):
        exit(1)

    main( argv[1] )