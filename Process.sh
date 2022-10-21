#!/bin/bash
if ! [ $# -eq 1 ];
then
    exit 1
fi

DATASETS="Large-scale_distributed_similarity_search_with_Locality-Sensitive_Hashing/data_sets"
PROCESSOR="Large-scale_distributed_similarity_search_with_Locality-Sensitive_Hashing/PostProcess/dataProcessor.py"
TESTFILE=$1
TESTFOLDER=$(dirname "$TESTFILE")
#TESTFOLDER="/"

TMP1="${TESTFILE%%.*}"
TMP2="${TMP1##*/}"

tar -xzvf "$TESTFILE" -C "$TESTFOLDER"
python3 "$PROCESSOR" "$TESTFOLDER/TESTS/$TMP2" "$DATASETS"
tar -czvf "$TESTFOLDER/$TMP2.processed.tar.gz" -C "$TESTFOLDER/TESTS/" "$TMP2"
rm -r "$TESTFILE" "$TESTFOLDER/TESTS"