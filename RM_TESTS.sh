#!/bin/bash
if [ $# -eq 0 ];
then
    exit 1
fi

for TESTFILE in "$@"
do
    TESTFOLDER=$(dirname "$TESTFILE")
    #TESTFOLDER="/"

    TMP1="${TESTFILE%%.*}"
    TMP2="${TMP1##*/}"

    tar -xzvf "$TESTFILE" -C "$TESTFOLDER"
    rm "$TESTFILE"
    tar -czvf "$TESTFOLDER/$TMP2.tar.gz" -C "$TESTFOLDER/TESTS" "$TMP2"
    rm -r "$TESTFOLDER/TESTS"
    #rm -r "$TESTFILE"
done