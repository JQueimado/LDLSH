#!/bin/bash

if [ $# -lt 1 ];
then
    exit 1
fi

Normal="creategraph_groundthruth.gnu"

cp "$Normal" "$1/."

( 
    cd "$1" || exit
    gnuplot "$Normal"
)