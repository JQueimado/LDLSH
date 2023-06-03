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

    datasetName = renameDataset( testsFolder )

    latencyTestResults = "/TestLatencyAverage_"+datasetName+".csv"
    accuracyTestResults = "/TestAccuracyAverage_"+datasetName+".csv"
    throughputTestResults = "/TestThroughputyAverage_"+datasetName+".csv"
    
    thresholdLatencyTestResults = "/TestThresholdxLatency_"+datasetName+".csv"
    thresholdAccuracyTestResults = "/TestThresholdxAccuracy_"+datasetName+".csv"
    thresholdThroughputTestResults = "/TestThresholdxThroughput_"+datasetName+".csv"

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
    queryLatencydf.to_csv( testsFolder + latencyTestResults)

    print("Accuracy:\n",queryAccuracydf)
    queryAccuracydf.to_csv( testsFolder + accuracyTestResults )

    print("Throughput:\n", queryThroughputdf)
    queryThroughputdf.to_csv( testsFolder + throughputTestResults )

    #Latency with threshold
    queryLatencyThresholddf = addThreshold(queryLatencydf)
    print("Latency with Threshold:\n",queryLatencyThresholddf)
    queryLatencyThresholddf.to_csv(testsFolder + thresholdLatencyTestResults, index=None, header=None, sep="\t")
    splitModels( testsFolder + thresholdLatencyTestResults ) 

    #Accuracy with threshold
    queryAccuracyThresholddf = addThreshold( queryAccuracydf )
    print("Accuracy with Threshold\n", queryAccuracyThresholddf)
    queryAccuracyThresholddf.to_csv(testsFolder + thresholdAccuracyTestResults, index=None, header=None, sep="\t")
    splitModels( testsFolder + thresholdAccuracyTestResults )

    #Througput with threshold
    queryThroughputThresholddf = addThreshold(queryThroughputdf)
    print("Threshold with througput\n", queryThroughputThresholddf)
    queryThroughputThresholddf.to_csv(testsFolder + thresholdThroughputTestResults, index=None, header=None, sep="\t")
    splitModels( testsFolder + thresholdThroughputTestResults )

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

def renameDataset( testfolder : str ) -> str :

    if(testfolder.endswith("/")):
        testfolder = testfolder[:-1]

    c = os.path.basename(testfolder)
    print(c)

    if( c == "I-SRR10000shufaa_Q-SRR10000shufaa_IT-10" ):
        return "10a-10a"
    elif ( c == "I-SRR10000shufaa_Q-SRR10000shufab_IT-10"):
        return "10a-10b"
    elif ( c == "I-SRR10000shufaa_Q-SRR10000shufac_IT-10" ):
        return "10a-10c"
    
    elif ( c == "I-SRR100000shufaa_Q-SRR100000shufaa_IT-10" ):
        return "100a-100a"
    elif ( c == "I-SRR100000shufaa_Q-SRR100000shufab_IT-10" ):
        return "100a-100b"
    elif ( c == "I-SRR100000shufaa_Q-SRR100000shufac_IT-10" ):
        return "100a-100c"

    elif ( c == "I-SRR100000shufaax10_Q-SRR100000shufaax10" ):
        return "10x10a-10x10a"
    elif ( c == "I-SRR100000shufaax10_Q-SRR100000shufabx10" ):
        return "10x10a-10x10b"
    elif ( c == "I-SRR100000shufaax10_Q-SRR100000shufacx10" ):
        return "10x10a-10x10c"

#__main__#
if __name__ == "__main__":
    argv = sys.argv
    if( len(argv) != 2 ):
        exit(1)

    main( argv[1] )