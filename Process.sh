#!/bin/bash
if [ $# -eq 0 ];
then
    exit 1
fi

DATASETS="/media/sf_Data/data_sets"
PROCESSOR="./PostProcess/dataProcessor.py"
for TESTFILE in "$@"
do
    TESTFOLDER=$(dirname "$TESTFILE")
    #TESTFOLDER="/"

    TMP1="${TESTFILE%%.*}"
    TMP2="${TMP1##*/}"

    tar -xzvf "$TESTFILE" -C "$TESTFOLDER"
    python3 "$PROCESSOR" "$TESTFOLDER/$TMP2" "$DATASETS"
    #tar -czvf "$TESTFOLDER/$TMP2.processed.tar.gz" -C "$TESTFOLDER/" "$TMP2"
    #rm -r "$TESTFILE" "$TESTFOLDER/$TMP2"
done