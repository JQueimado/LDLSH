#!/bin/bash
if [ $# -eq 0 ];
then
    exit 1
fi

DATASETS="/home/joao/Tese/Large-scale_distributed_similarity_search_with_Locality-Sensitive_Hashing/data_sets"
PROCESSOR="/home/joao/Tese/Large-scale_distributed_similarity_search_with_Locality-Sensitive_Hashing/PostProcess/dataProcessor_cp.py"
for TESTFILE in "$@"
do
    TESTFOLDER=$(dirname "$TESTFILE")
    #TESTFOLDER="/"

    TMP1="${TESTFILE%%.*}"
    TMP2="${TMP1##*/}"

    tar -xzvf "$TESTFILE" -C "$TESTFOLDER"
    python3 "$PROCESSOR" "$TESTFOLDER/$TMP2" "$DATASETS"
    tar -czvf "$TESTFOLDER/$TMP2.processed.tar.gz" -C "$TESTFOLDER/" "$TMP2"
    rm -r "$TESTFILE" "$TESTFOLDER/$TMP2"
done