#!/bin/bash

if [ $# -lt 1 ];
then
    exit 1
fi

# LIST=$(ls "$1")
(
    cd "$1" || exit 1
    for DATA in *;
    do
        echo "$DATA"

        (
            cd "$DATA" ||exit 0

            LIST=$(ls | grep "accuracy_q")
            # echo "$LIST"
            for DATA2 in $LIST;
            do
                LEN=$(grep -c "null" < "$DATA2")
                echo "$DATA2" "$LEN"
            done
        )
    done
)