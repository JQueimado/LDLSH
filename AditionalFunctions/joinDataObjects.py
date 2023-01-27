import sys

def main( fname : str ):
    dfname = fname.split(".")[0] + "x10"
    with open(fname, 'r') as rf:
        with open(dfname, "x") as wf:
            c = 0
            s = ""
            while True:
                l = rf.readline()
                if not l:
                    break
                s+=l[:-1]
                c+=1
                if( c >= 10):
                    wf.write(s+"\n")
                    s=""
                    c=0
    pass

if(__name__ == "__main__"):
    argv = sys.argv
    if( len(argv) != 2 ):
        exit(1)
    main( argv[1] )
    pass