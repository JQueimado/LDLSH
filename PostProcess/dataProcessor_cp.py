from fileinput import filename
import sys
import os
import pandas as pd
import numpy as np
import threading as thr

levels = [4,3,2,1]

#Accuracy Aux
def createNgram( s : str, l : int ):
    r = set()
    c = len(s) -l +1
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

def accuracyProcessor( testfiles :list, datasetfname: str, ngramLevel: int ):
    resultdf : pd.DataFrame = pd.DataFrame()

    testfiles.sort()

    for file in testfiles:
        print(file)
        # INSERT
        if "_i_" in file:
            df = pd.read_csv(file, sep=' ', header=None)
            df = df.drop(0, axis=1)
            df.columns = ['result']
            df = df.sort_values(by=['result'])
            df = df.reset_index(drop=True)

            dsdf = pd.read_csv(datasetfname, sep=' ', header=None)
            dsdf.columns = ['value']
            dsdf = dsdf.sort_values(by=['value'])
            dsdf = dsdf.reset_index(drop=True)

            df['value'] = dsdf['value']

            df['eval'] = np.where(df['value'] == df['result'], True, False)
            df.head()

            df = df.drop("value", axis=1)
            df.columns = ["value", "result"]

        elif "_q_" in file:
            df = pd.read_csv(file, sep=' ', header=None)
            df = df.drop(1, axis=1)
            df.columns = ['value', 'result']

            df['jaccard distance'] = df.apply( lambda row : jcDistance(row['value'], row['result'], ngramLevel), axis=1 )
            df.head()
            df = df.drop("result", axis=1)
            df.columns = ["value", "result"]

        fileName = os.path.basename(file)
        if resultdf.empty:
            resultdf = df.rename(columns={ 'result' : fileName })
            #resultdf = resultdf.set_index('value')
        else:
            resultdf = pd.merge(resultdf, df, on='value', how="outer" )
            #resultdf = pd.merge(resultdf, df, on='value')
            resultdf = resultdf.rename(columns={ 'result' : fileName })

    dirName : str = os.path.dirname(testfiles[0])
    
    resultdf = resultdf.set_index('value')
    print(resultdf)
    resultdf.to_csv( dirName+"/accuracy.results.csv" )
    
    stats = resultdf.agg(["min", "max", "median", "std", "mean", "skew"])
    
    print(stats)
    stats.to_csv( dirName+"/accuracy.stats.csv")


#LatencyProcessor:
# testfname: str -> filename
def latencyProcessor( testfiles : list):

    resultdf : pd.DataFrame = pd.DataFrame()

    testfiles.sort()

    for file in testfiles:
        print(file)

        df = pd.read_csv(file, sep=" ", header=None)
        df = df.drop([0,1,3,4,6], axis=1)
        df.columns = ['value', 'time']
        df = df.sort_values(by='value')

        fileName = os.path.basename(file)
        if resultdf.empty:
            resultdf = df.rename(columns={ 'time' : fileName })
            #resultdf = resultdf.set_index('value')
        else:
            resultdf = pd.merge(resultdf, df, on='value', how="outer" )
            #resultdf = pd.merge(resultdf, df, on='value')
            resultdf = resultdf.rename(columns={ 'time' : fileName })

    dirName : str = os.path.dirname(testfiles[0])
    
    resultdf = resultdf.set_index('value')
    print(resultdf)
    resultdf.to_csv( dirName+"/latency.results.csv" )
    
    stats = resultdf.agg(["min", "max", "median", "std", "mean", "skew"])
    
    print(stats)
    stats.to_csv( dirName+"/latency.stats.csv")

#ThroughputProcessor:
# testfname: [] -> list of files to process
def throughputProcessor( files : list, dir : str ):

    files.sort()

    df = pd.DataFrame({'total time':[], 'throughput':[]})

    for file in files:
        print(file)
        temp_df = pd.read_csv(file, sep=" ", header=None)
        temp_df = temp_df.drop([0,2], axis=1)
        df.loc[len(df.index)] = [ temp_df[1][0], temp_df[1][1] ]

    df.to_csv( dir + "/throughput.results.csv" )
    stats = df.agg({
        'total time': ["min", "max", "median", "std", "mean", "skew"],
        'throughput': ["min", "max", "median", "std", "mean", "skew"]
    })
    stats.to_csv(dir + "/throughput.stats.csv")

if __name__ == "__main__":
    argv = sys.argv
    if( len(argv) != 3 ):
        exit(1)

    test = argv[1]
    datasetFolder = argv[2]
    if( not datasetFolder.endswith("/") ):
        datasetFolder = datasetFolder + "/"

    test_name = test.split("/")[-1]

    #Namingformat:
    # <TEST-Name>_<I-InsertData>_<Q-QueryData>_<IT-Iterations>
    test_data = test_name.split("_")

    test_contents = os.listdir(test)

    accuracyFiles = []
    latencyFiles = []
    throughputFiles = []

    #Dataset file
    insert_dataset = datasetFolder + test_data[1].split("-")[1]

    #Ngram level
    test_n = int( test_data[0].split("-")[-1] )
    level = 0
    
    if( test_n < 6 ):
        level = levels[0]
    else:
        level = levels[test_n - 6]

    #Sort Files
    for file in test_contents:
        if( (not file.endswith(".txt")) ):
            continue

        print( file )

        #Namingformat:
        # test_<type>_<op>_<timestamp>.txt
        test_split = file.split("_")
        test_type = test_split[1]

        file = test + "/" + file

        if( test_type == "accuracy" ):
            accuracyFiles.append(file)

        elif( test_type == "latency" ):
            latencyFiles.append(file)
        
        elif( test_type == "throughput" ):
            throughputFiles.append(file)

    #Process files 
    #accuracyProcessor( accuracyFiles, insert_dataset, level )
    accuracy_thread = thr.Thread( target=accuracyProcessor, args=(accuracyFiles, insert_dataset, level) )
    accuracy_thread.start()

    #latencyProcessor(latencyFiles)
    latency_thread = thr.Thread( target=latencyProcessor, args=(latencyFiles,) )
    latency_thread.start()
    
    #throughputProcessor( throughputFiles, test )
    throughput_thread = thr.Thread( target=throughputProcessor, args=(throughputFiles, test) )
    throughput_thread.start()

    accuracy_thread.join()
    latency_thread.join()
    throughput_thread.join()