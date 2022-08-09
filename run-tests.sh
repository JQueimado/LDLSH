#script is ment to run on the testing enviroment
HOST="t5" 
CONFIGSFOLDER="LDLSH"

run_java(){
    DIRCONFIG=$1
    OP=$2
    DATAFILE=$3
    nohup java -server -Xmx100g -XX:+UseG1GC -jar LDLSH-3.2.jar $DIRCONFIG/ClientNode-$HOST.quinta.properties $OP $DATAFILE
}

LDLSH_Accuracy_Test(){
    ITERATIONS=$1
    DATAFILE=$2

    ./run_quinta_server all --change_branch Test-Accuracy
    ./run_quinta_server all -js

    for IT in 1 .. $ITERATIONS
    do
        run_java "{$CONFIGSFOLDER}/Demo"
    done
}