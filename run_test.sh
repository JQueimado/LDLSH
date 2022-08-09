if [ $# -eq 3 ]
then
    OP=$1
    DATAFILE=$2
    RESULTSFOLDER=$3
    TIMESTAMP=`date +%s`
    java -server -Xmx100g -XX:+UseG1GC -jar LDLSH-3.2.jar ${DIRCONFIG}/ClientNode.properties ${OP} ${DATAFILE}  >> ${RESULTSFOLDER}/test_${OP}_${TIMESTAMP}.txt
else
    exit 1
fi