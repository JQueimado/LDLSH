#!/bin/bash

#if [ $# -lt 1 ];
#then
#    exit 1
#fi

#FILES=$( ls "$1" | grep "AccuracyxNgram")

#echo "$FILES"

python3 PostProcess/joinData.py \
    /media/sf_Data/TESTS-t4/TestResults/TestAccuracyxNgram_10a-10b.csv \
    /media/sf_Data/TESTS-t4/TestResults/TestAccuracyxNgram_10a-10c.csv \
    /media/sf_Data/TESTS-t4/TestResults/TestAccuracyxNgram_10.csv

python3 PostProcess/joinData.py \
    /media/sf_Data/TESTS-t4/TestResults/TestAccuracyxNgram_100a-100b.csv \
    /media/sf_Data/TESTS-t4/TestResults/TestAccuracyxNgram_100a-100c.csv \
    /media/sf_Data/TESTS-t4/TestResults/TestAccuracyxNgram_100.csv

python3 PostProcess/joinData.py \
    /media/sf_Data/TESTS-t4/TestResults/TestAccuracyxNgram_10x10a-10x10b.csv \
    /media/sf_Data/TESTS-t4/TestResults/TestAccuracyxNgram_10x10a-10x10c.csv \
    /media/sf_Data/TESTS-t4/TestResults/TestAccuracyxNgram_10x10.csv