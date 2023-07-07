#!/bin/bash

if [ $# -lt 1 ];
then
    exit 1
fi

Normal="createGraphs.gnu"
Ngram="createGraphsNgrams.gnu"
Candidates="candidates_sizes.gnu"
Memory="memory_graphs.gnu"

cp "$Normal" "$1/."
cp "$Ngram" "$1/."
cp "$Candidates" "$1/."
cp "$Memory" "$1/."

( 
    cd "$1" || exit
    gnuplot "$Normal"
    gnuplot "$Ngram"
    gnuplot "$Candidates"
    gnuplot "$Memory"
    rm "$Normal" "$Ngram" "$Candidates" "$Memory"
    #mkdir "pdf"
    #mv ./*.pdf pdf/.
)