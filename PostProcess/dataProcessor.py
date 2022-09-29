import sys
import os
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

def accuracyInsertProcessor( testfname : str , datasetfname : str ):
    #Read Dataset
    dsdf = pd.read_csv(datasetfname, sep=' ', header=None)
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

def accuracyQueryProcessor( testfname : str , ngramLevel : int ):
    #Read Dataset
    dsdf = pd.read_csv(testfname, sep=' ', header=None)
    dsdf.columns = ['query', 'sep', 'result']
    dsdf = dsdf.drop(columns=['sep'])

    dsdf['jaccard distance'] = dsdf.apply( lambda row : jcDistance(row['query'], row['result'], ngramLevel), axis=1 )
    dsdf.head()

    dsdf.to_csv( testfname + ".acc.results.csv" )

    #print( "avg: " + str( dsdf['jaccard distance'].mean() ))
    stats = dsdf.agg({
        'jaccard distance': ["min", "max", "median", "skew", "mean"]
    })

    stats.to_csv(testfname + ".acc.stats.csv")

def latencyProcessor( testfname : str ):
    df = pd.read_csv(testfname, sep=" ", header=None)
    df = df.drop([0,1,3,4,6], axis=1)
    df.columns = ["val", "time"]

    df.to_csv( testfname + ".lat.results.csv" )

    stats = df.agg({
        'time': ["min", "max", "median", "skew", "mean"]
    })

    stats.to_csv(testfname + ".lat.stats.csv")

def throughputProcessor( testfname: str ):
    df = pd.read_csv(testfname, sep=" ", header=None)
    df = df.drop([2], axis=1)

    df.to_csv( testfname + ".thr.results.csv" )

if __name__ == "__main__":
    argv = sys.argv
    if( len(argv) != 2 ):
        exit(1)

    test = argv[1]

    #Namingformat:
    # <TEST-Name>_<I-InsertData>_<Q-QueryData>_<IT-Iterations>
    test_data = test.split("_")

    test_contents = os.listdir(test)

    throughputFiles = []

    for file in test_contents:
        if( (not file.endswith(".txt")) or (not os.path.isfile(file)) ):
            continue
        
        #Namingformat:
        # test_<type>_<op>_<timestamp>.txt
        test_split = file.split("_")
        test_type = test_split[1]

        if( test_type == "accuracy" ):
            test_op = test_split[2]
            if( test_op == "i" ):
                insert_dataset = test_data[1].split("-")[1]
                accuracyInsertProcessor(file, insert_dataset)
            elif( test_op == "q" ):
                test_name = int( test_data[0].split("-")[1] )

        elif( test_type == "latency" ):
            latencyProcessor( file )
        elif( test_type == "throughput" ):
            throughputFiles.append(file)




