from doctest import testfile
import sys
import pandas as pd
import numpy as np

#Accuracy Aux
def createNgram( s : str, l : int ):
    r = set()
    c = len(s) -l -1
    for i in range(c):
        ng = s[i:i+l]
        r.add(ng)
    return r


def jcDistance( a : str, b : str, l : int ):
    if( type(b) == float ):
        return 1
    a = createNgram(a, l)
    b = createNgram(b, l)
    return 1 - float(len(a.intersection(b)))/len(a.union(b))

#usages: 
# - python dataProcessor.py -a -i <tetsFile> <dataSetFile>
# - python dataProcessor.py -a -q <tetsFile> <dataGramLevel>
def accuracyProcessor( op : str, testfname : str , aditionalArgument : str ):

    if( op == '-i' ):
        #Read Dataset
        dsdf = pd.read_csv(aditionalArgument, sep=' ', header=None)
        dsdf.columns = ['dastasetValue']

        #Read Results
        tfdf = pd.read_csv(testfname, sep=' ', header=None)
        tfdf = tfdf.drop(0, axis=1)
        tfdf.columns = ['insertedValue']

        dsdf = dsdf.sort_values(by=['dastasetValue'])
        dsdf = dsdf.reset_index(drop=True)

        tfdf = tfdf.sort_values(by=['insertedValue'])
        tfdf = tfdf.reset_index(drop=True)

        dsdf['insertedValue'] = tfdf['insertedValue']

        dsdf['eval'] = np.where(dsdf['dastasetValue'] == dsdf['insertedValue'], True, False)
        dsdf.head()

        dsdf.to_csv( testfname + ".acc.results.csv")

        stats = dsdf.agg({
            'eval': ["min", "max", "median", "skew", "mean"]
        })

        stats.to_csv(testfname + ".acc.stats.csv")

    elif( op == '-q' ):
        l = int(aditionalArgument)
        #Read Dataset
        dsdf = pd.read_csv(testfname, sep=' ', header=None)
        dsdf.columns = ['query', 'sep', 'result']
        dsdf = dsdf.drop(columns=['sep'])

        dsdf['jaccard distance'] = dsdf.apply( lambda row : jcDistance(row['query'], row['result'], l), axis=1 )
        dsdf.head()

        dsdf.to_csv( testfname + ".acc.results.csv" )

        #print( "avg: " + str( dsdf['jaccard distance'].mean() ))
        stats = dsdf.agg({
            'jaccard distance': ["min", "max", "median", "skew", "mean"]
        })

        stats.to_csv(testfname + ".acc.stats.csv")

#usage
# - python3 dataProcessor -l <testFile>
def latencyProcessor( testfname : str ):
    df = pd.read_csv(testfname, sep=" ", header=None)
    df = df.drop([0,1,3,4,6], axis=1)
    df.columns = ["val", "time"]

    df.to_csv( testfname + ".lat.results.csv" )

    stats = df.agg({
        'time': ["min", "max", "median", "skew", "mean"]
    })

    stats.to_csv(testfname + ".lat.stats.csv")

#usage
# - python3 dataProcessor -t <testFile>
def throughputProcessor( testfname: str ):
    df = pd.read_csv(testfname, sep=" ", header=None)
    df = df.drop([2], axis=1)

    df.to_csv( testfname + ".thr.results.csv" )

#usage: 
# - python dataProcessor.py <test> [*args]
if __name__ == "__main__":
    argv = sys.argv
    la = len(argv)
    if( la < 2 ):
        exit(1)

    test = argv[1]

    if( test == "-a" ):
        if( la != 5 ):
            exit(1)
        accuracyProcessor( argv[2], argv[3], argv[4] )
    elif( test == "-l" ):
        if( la != 3 ):
            exit(1)
        latencyProcessor(argv[2])
    elif( test == "-t" ):
        if( la != 3 ):
            exit(1)
        throughputProcessor(argv[2])


