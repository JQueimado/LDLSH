import os,sys
import pandas as pd
import threading as thr

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
        level: int, 
        print_flag : bool = False,
        row : int = -1, 
        size: int = -1 
    ) -> float:

    if(print_flag):
        pc = (row/size)*100
        st ="Thread_1: {percent:.2f}%".format(percent=pc)
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
def processData( query_df : pd.DataFrame, insert_df : pd.DataFrame, ngramLevel : int, flag : bool = False ) -> pd.DataFrame:
    query_df["best_distance"] = query_df.apply( lambda row : bestDistance( row['query_values'], insert_df, ngramLevel, flag, row.name, query_df.shape[0]), axis=1 )
    query_df.head()
    print("Results:\n",query_df)
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
    resultsFileName += ".csv"
    resultsFileName = os.path.dirname(insertData) + "/" + resultsFileName
    print( "will save file in: " + resultsFileName)

    #insert data
    insert_df = pd.read_csv(insertData, sep=" ", header=None)
    insert_df.columns = ["insert_values"]

    #query data
    query_df = pd.read_csv(queryData, sep=" ", header=None)
    query_df.columns = ["query_values"]

    #Split data set
    row_split = int((query_df.shape[0])/4)
    query_df0 = query_df.iloc[ : row_split, : ]
    query_df1 = query_df.iloc[ row_split : 2*row_split, : ]
    query_df2 = query_df.iloc[ 2*row_split : 3*row_split, : ]
    query_df3 = query_df.iloc[ 3*row_split : , : ]

    #distance calculation
    thread_1 = thr.Thread( target=processData, args=(query_df0, insert_df, ngramLevel, True))
    thread_1.start()
    thread_2 = thr.Thread( target=processData, args=(query_df1, insert_df, ngramLevel))
    thread_2.start()
    thread_3 = thr.Thread( target=processData, args=(query_df2, insert_df, ngramLevel))
    thread_3.start()
    thread_4 = thr.Thread( target=processData, args=(query_df3, insert_df, ngramLevel))
    thread_4.start()

    thread_1.join()
    thread_2.join()
    thread_3.join()
    thread_4.join()

    #Concat data set
    new_query_df = pd.concat( [query_df0, query_df1, query_df2, query_df3] )
    print(new_query_df)

    #Save results
    query_df.to_csv( resultsFileName )

    pass