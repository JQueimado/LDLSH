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
    if( len(argv) != 4 ):
        exit(1)

    #print(argv)

    data1 = pd.read_csv(argv[1], header=None, sep='\t')
    data2 = pd.read_csv(argv[2], header=None, sep='\t')

    data1 = data1.join(data2.set_index(1), on=1, lsuffix="_d1", rsuffix="_d2")
    data1.drop("0_d2", axis=1, inplace=True)

    data1['mean'] = data1.apply( lambda row : (row["2_d1"] + row["2_d2"])/2, axis=1 )
    data1['meanplus'] = data1.apply( lambda row : (row["3_d1"] + row["3_d2"])/2, axis=1 )
    data1['meanminus'] = data1.apply( lambda row : (row["4_d1"] + row["4_d2"])/2, axis=1 )

    data1.drop( ["2_d1","2_d2","3_d1","3_d2","4_d1","4_d2"], axis=1, inplace=True )

    data1.to_csv(argv[3], header=None, index=None, sep="\t")
    splitModels( argv[3], 2 )

    print(data1)
    #print(data2)
