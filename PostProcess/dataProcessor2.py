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

    for test in tests:
        test = testsFolder + test
        #check dir
        if not os.path.isdir(test):
            continue

        #add directory extencion
        if ( not test.endswith("/") ):
            test = test + "/"

        currentdf = pd.read_csv( test + "latency.mean.csv", header=None )
        currentdf = currentdf.iloc[1:]
        
        testname = os.path.basename( test[:-1] ).split("_")[0]

        mean = currentdf[1].mean()
        std = currentdf[1].std()
        
        queryLatencydf.loc[len(queryLatencydf)] = [testname, mean, mean + std, mean - std]

    print(queryLatencydf)
    queryLatencydf.to_csv( testsFolder+"/TestXLatencyAverage.csv")

    for i, row in queryLatencydf.iterrows():
        
        threshold : float = 0.0
        test : str = row["test"]

        if( test.endswith("test-6") ):
            queryLatencydf.drop([i], axis=0, inplace=True)
            continue

        if( test.endswith("test-2") ):
            threshold = 0.75
        if( test.endswith("test-3") ):
            threshold = 0.90
        if( test.endswith("test-4") ):
            threshold = 0.95
        
        queryLatencydf.at[i,"threshold"] = threshold
    queryLatencydf.reset_index(drop=True, inplace=True)

    queryLatencydf.drop("test", axis=1, inplace=True)
    print(queryLatencydf)
    queryLatencydf.to_csv(testsFolder+"/ThresholdxLatency.csv", index=None, header=None, sep="\t")

#__main__#
if __name__ == "__main__":
    argv = sys.argv
    if( len(argv) != 2 ):
        exit(1)

    main( argv[1] )