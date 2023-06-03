#!/bin/bash
if [ $# -eq 0 ];
then
    exit 1
fi

PROCESSOR="./PostProcess/dataProcessor2.py"
for TEST in "$@"
do
    #Check if is dir
    if [ ! -d "$TEST" ];then
        continue
    fi

    if [ "$TEST" = "TestResults" ]; then
        continue
    fi

    python3 "$PROCESSOR" "$TEST"

    TESTRESULTS="$(dirname "$TEST")/TestResults"
    
    if [ ! -d "$TESTRESULTS" ]; then
        mkdir "$TESTRESULTS"
    fi

    find "$TEST" -name "Test*.csv" | xargs -I '{}' mv {} "$TESTRESULTS"

done