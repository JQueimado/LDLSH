from fileinput import filename
import sys
import os
import pandas as pd
import numpy as np

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

#__main__#
if __name__ == "__main__":
    argv = sys.argv
    if( len(argv) != 2 ):
        exit(1)

    main( argv[1] )