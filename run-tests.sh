#script is ment to run on the testing enviroment
HOST="t5" 
CONFIGSFOLDER="LDLSH"

run_java(){
    DIRCONFIG=$1
    OP=$2
    DATAFILE=$3
    RESULTS=$4
    TIMESTAMP=`date +%s` 
    java -server -Xmx100g -XX:+UseG1GC -jar LDLSH-3.2.jar $DIRCONFIG/ClientNode.properties "-${OP}" $DATAFILE >> ${RESULTS}/test_${OP}_${DATAFILE}_${TIMESTAMP}.txt
}

LDLSH_Accuracy_Test(){
    ITERATIONS=$1
    INSERTFILE=$2
    QUERYFILE=$3
    CONFIGFILE="{$CONFIGSFOLDER}/LDLSH_Quinta"
    RESULTSFOLDER="LDLSH_ACCURACY_TESTS"

    #Setup
    ./run_quinta_server all --change_branch Test-Accuracy
    ./run_quinta_server all -js

    run_java $CONFIGFILE "i" $INSERTFILE $RESULTSFOLDER

    for IT in 1 .. $ITERATIONS
    do
        run_java $CONFIGFILE "q" $QUERYFILE $RESULTSFOLDER
    done
}

LDLSH_Accuracy_Test $1 $2 $3