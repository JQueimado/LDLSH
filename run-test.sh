if ! [ $# -eq 4 ];
then
   exit 1
fi

DIRCONFIG=$1
OP=$2
DATAFILE=$3
RESULTSFOLDER=$4
TIMESTAMP=$(date +%s)
FILENAME="${RESULTSFOLDER}/test_${OP}_${TIMESTAMP}.txt"
echo "Test to be stored in ${FILENAME}"
$(java -server -Xmx100g -XX:+UseG1GC -jar LDLSH-3.2.jar "${DIRCONFIG}/ClientNode.properties" "-${OP}" "${DATAFILE}") >> "${FILENAME}"