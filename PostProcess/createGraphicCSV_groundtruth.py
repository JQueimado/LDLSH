from fileinput import filename
import sys
import os
import pandas as pd

#Splits models for the gnuplot interface
def splitModels( fname : str ):
    with open( fname, "r" ) as f:
        lines = f.readlines()

    for i in range(2, len(lines), 3):
        lines[i] = lines[i] + "\n\n"

    with open( fname, "w") as f:
        f.writelines( lines )

def main2( folder ):
    files = os.listdir(folder)
    files.sort()

    ngram3df :pd.DataFrame = pd.DataFrame(columns=["name", "mean", "mean_plus", "mean_minus"])
    ngram4df :pd.DataFrame = pd.DataFrame(columns=["name", "mean", "mean_plus", "mean_minus"])


    for file in files:
        if not file.endswith("stats.csv"):
            continue

        currentdf = pd.read_csv(file)
        mean = currentdf.iloc[4].best_distance
        std = currentdf.iloc[3].best_distance

        name = renameDataset(file)

        if "3" in file:
            ngram3df.loc[len(ngram3df)] = [name, mean, mean+std, mean-std]
        else:
            ngram4df.loc[len(ngram4df)] = [name, mean, mean+std, mean-std]

    print(ngram3df)
    print(ngram4df)

    ngram3df.to_csv("srrl3.csv", sep="\t", header=None, index=None)
    ngram4df.to_csv("srrl4.csv", sep="\t", header=None, index=None)

    splitModels("srrl3.csv")
    splitModels("srrl4.csv")

def renameDataset( testfolder : str ) -> str :

    if( "SRR10000shufaa_SRR10000shufaa" in testfolder ):
        return "10a-10a"
    elif ( "SRR10000shufaa_SRR10000shufab" in testfolder ):
        return "10a-10b"
    elif ( "SRR10000shufaa_SRR10000shufac" in testfolder ):
        return "10a-10c"
    
    elif ( "SRR100000shufaa_SRR100000shufaa" in testfolder ):
        return "100a-100a"
    elif ( "SRR100000shufaa_SRR100000shufab" in testfolder ):
        return "100a-100b"
    elif ( "SRR100000shufaa_SRR100000shufac" in testfolder ):
        return "100a-100c"

    elif ( "SRR100000shufaax10_SRR100000shufaax10" in testfolder ):
        return "10x10a-10x10a"
    elif ( "SRR100000shufaax10_SRR100000shufabx10" in testfolder ):
        return "10x10a-10x10b"
    elif ( "SRR100000shufaax10_SRR100000shufacx10" in testfolder ):
        return "10x10a-10x10c"

#__main__#
if __name__ == "__main__":
    argv = sys.argv
    if( len(argv) != 2 ):
        exit(1)

    main2( argv[1] )