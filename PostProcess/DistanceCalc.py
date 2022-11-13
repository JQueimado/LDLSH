import sys
import pandas as pd
import numpy as np

level = 0

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
    intersect = a.intersection(b)
    union = a.union(b)

    print(a)
    print(b)
    print( intersect ) 
    print( union )

    return 1 - float(len(a.intersection(b)))/len(a.union(b))

if __name__ == "__main__":
    argv = sys.argv
    if len(argv) != 4:
        exit(1)
    
    level = int(argv[1])
    data1 = argv[2]
    data2 = argv[3]

    print( jcDistance(data1, data2, level) )
