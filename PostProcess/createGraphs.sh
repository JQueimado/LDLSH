#!/bin/bash

if [ $# -lt 1 ];
then
    exit 1
fi

Normal="createGraphs.gnu"
Ngram="createGraphsNgrams.gnu"
Candidates="candidates_sizes.gnu"

cp "$Normal" "$1/."
cp "$Ngram" "$1/."
cp "$Candidates" "$1/."

( 
    cd "$1" || exit
    gnuplot "$Normal"
    gnuplot "$Ngram"
    gnuplot "$Candidates"
    rm "$Normal" "$Ngram" "$Candidates"
    #mkdir "pdf"
    #mv ./*.pdf pdf/.
)