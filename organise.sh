#!/bin/bash

if [ $# -lt 1 ];
then
    exit 1
fi

FOLDER=$1

DATASETS=(
    "I-SRR10000shufaa_Q-SRR10000shufaa_IT-10"
    "I-SRR10000shufaa_Q-SRR10000shufab_IT-10"
    "I-SRR10000shufaa_Q-SRR10000shufac_IT-10"
    "I-SRR100000shufaa_Q-SRR100000shufaa_IT-10"
    "I-SRR100000shufaa_Q-SRR100000shufab_IT-10"
    "I-SRR100000shufaa_Q-SRR100000shufac_IT-10"
    "I-SRR100000shufaax10_Q-SRR100000shufaax10"
    "I-SRR100000shufaax10_Q-SRR100000shufabx10"
    "I-SRR100000shufaax10_Q-SRR100000shufacx10" 
    )

cd "$FOLDER"
for SET in "${DATASETS[@]}"
do
    mkdir "$SET"
    mv $(ls . | grep "_$SET") "$SET/"
done