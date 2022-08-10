if [ $# -eq 3 ]
then
    DIRCONFIG=$1
    OP=$2
    DATAFILE=$3
    RESULTSFOLDER=$4
    TIMESTAMP=`date +%s`
    java -server -Xmx100g -XX:+UseG1GC -jar LDLSH-3.2.jar ${DIRCONFIG}/ClientNode.properties ${OP} ${DATAFILE} >> ${RESULTSFOLDER}/test_${OP}_${TIMESTAMP}.txt
else
    exit 1
fi