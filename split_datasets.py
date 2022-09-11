from ast import arg
import os
import sys
import numpy as np
import pandas as pd

argv = sys.argv

if len(argv) != 3:
    os._exit(1)

dataFile = argv[1]
queryPercent = int(argv[2])

dataFrame = pd.read_csv(dataFile, sep=" ", header=None)

print("Full dataset:")
print(dataFrame)

query_sample = dataFrame.sample(frac=(queryPercent/100))

insert_sample = dataFrame.drop(query_sample.index)

print("Query dataset:")
print(query_sample)
print("insert dataset:")
print(insert_sample)

fname = dataFile.split('/')[-1]

if fname.endswith(".txt"):
    fname = fname[:-4]

folder = fname+"_"+str(queryPercent)+"%_split"
os.makedirs(folder)

np.savetxt("./"+folder+"/"+fname+"_insert.txt", insert_sample.values, fmt='%s')
np.savetxt("./"+folder+"/"+fname+"_query.txt", query_sample.values, fmt='%s')