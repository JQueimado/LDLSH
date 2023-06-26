import sys
import pandas as pd

def splitModels( fname :str, i :int ):
    with open( fname, "r" ) as f:
        lines = f.readlines()

    for i in range(i-1, len(lines), i):
        lines[i] = lines[i] + "\n\n"

    with open( fname, "w") as f:
        f.writelines( lines )


if __name__ == "__main__":
    argv = sys.argv
    if( len(argv) != 5 ):
        exit(1)

    #print(argv)

    data1 = pd.read_csv(argv[1], header=None, sep='\t')
    data2 = pd.read_csv(argv[2], header=None, sep='\t')

    #print(data1)
    #print(data2)

    data1 = data1.join(data2, lsuffix="_d1", rsuffix="_d2")
    #data1.drop("0_d2", axis=1, inplace=True)
    #print( data1 )

    data1['mean'] = data1.apply( lambda row : (row["0_d1"] + row["0_d2"])/2, axis=1 )

    data1.drop( ["0_d1","0_d2","1_d2"], axis=1, inplace=True )

    index = data1.columns.to_list()
    index = index[1:] + index[0:1]

    data1 = data1.reindex(columns=index)
    # print( data1 )

    data1.to_csv(argv[3], header=None, index=None, sep="\t")
    splitModels( argv[3], int(argv[4]) )

    print(data1)
    #print(data2)
