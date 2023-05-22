import os,sys
import pandas as pd
import threading as thr

percent = [0,0]
lock = thr.Lock()

#Creates a set of ngrams for a given vector
def createNgram( s : str, l : int ):
    r = set()
    c = len(s) -l +1
    for i in range(c):
        ng = s[i:i+l]
        r.add(ng)
    return r

#Calculates the jaccard Distance
def jcDistance( a : str, b : str, l : int ):
    if( type(b) == float ):
        return 1
    a = createNgram(a, l)
    b = createNgram(b, l)
    return 1 - float(len(a.intersection(b)))/len(a.union(b))

#Calculates the distance betwen a vector and its best candidate in a given dataset
def bestDistance( 
        subject : str, 
        data : pd.DataFrame, 
        level: int
    ) -> float:

    with lock:
        percent[0] += 1
        pc = (percent[0]/percent[1])*100
        st ="Complete: {c}/{t} -> {p:.02f}%".format( c=percent[0], t=percent[1], p=pc)
        print( st, end='\r')

    bestDistance = 1.0

    for _, row in data.iterrows(): 
        currentValue = row["insert_values"]
        distance = jcDistance( currentValue, subject, level )

        if( distance < bestDistance ):
            bestDistance = distance
        if(distance == 0 ):
            break

    return bestDistance

#Processess a dataframe
def processData( 
        query_df : pd.DataFrame, 
        insert_df : pd.DataFrame,
        ngramLevel : int
    ) -> pd.DataFrame:
    
    query_df["best_distance"] = query_df.apply( lambda row : bestDistance( row['query_values'], insert_df, ngramLevel), axis=1 )
    query_df.head()
    return query_df

#__main__#
if __name__ == "__main__":

    argv = sys.argv
    if( len(argv) != 4):
        exit(1)

    #Arguments
    insertData = argv[1]
    queryData = argv[2]
    ngramLevel = int( argv[3] )

    #result file name
    resultsFileName = os.path.basename(insertData)
    resultsFileName += "_"
    resultsFileName += os.path.basename(queryData)
    resultsFileName +="_"
    resultsFileName += str(ngramLevel) 
    resultsStatsFilename = resultsFileName + "_stats.csv"
    resultsFileName += ".csv"
    resultsFileName = os.path.dirname(insertData) + "/" + resultsFileName
    resultsStatsFilename = os.path.dirname(insertData) + "/" + resultsStatsFilename

    #insert data
    insert_df = pd.read_csv(insertData, sep=" ", header=None)
    insert_df.columns = ["insert_values"]

    #query data
    query_df = pd.read_csv(queryData, sep=" ", header=None)
    query_df.columns = ["query_values"]

    #Split data set
    split = 16
    dataFrames = []
    threads = []
    percent = [0, query_df.shape[0]]

    row_split = int(percent[1]/split)

    for i in range(split):
        cdataFrame = query_df.iloc[ i*row_split : (i+1)*row_split, : ]
        dataFrames.append(cdataFrame)

        flag = False
        if(i == 0):
            flag = True
        
        ctherad = thr.Thread( target=processData, args=(cdataFrame, insert_df, ngramLevel) )
        ctherad.start()
        threads.append(ctherad)

    #wait
    for i in range(split):
        threads[i].join()


    #Concat data set
    new_query_df = pd.concat( dataFrames )
    print(new_query_df)

    #Save results
    new_query_df.to_csv( resultsFileName )

    stats_df = new_query_df.agg({
        'best_distance' : ["min", "max", "median", "std", "mean"]
    })

    stats_df.to_csv( resultsStatsFilename )

    pass