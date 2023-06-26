#!/bin/bash

if [ $# -lt 1 ];
then
    exit 1
fi

Normal="createGraphs.gnu"
Ngram="createGraphsNgrams.gnu"

cp "$Normal" "$1/."
cp "$Ngram" "$1/."

( 
    cd "$1" || exit
    gnuplot "$Normal"
    gnuplot "$Ngram"
    rm "$Normal" "$Ngram"
    #mkdir "pdf"
    #mv ./*.pdf pdf/.
)