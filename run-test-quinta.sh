#env
DIR="/root/jqueimado/Large-scale_distributed_similarity_search_with_Locality-Sensitive_Hashing"

build(){
	ssh "$1" "cd ${DIR}; ./run-server.sh -b"
}

change_branch(){
	ssh "$1" "cd ${DIR}; git checkout $2"
	git_pull "$1"
}

git_pull(){
	ssh "$1" "cd ${DIR}; git pull;"
}

run_server_jar(){
 	ssh "$1" "cd ${DIR}; ./run-server.sh -js"
}

run_test_client_jar(){
	ssh "$1" "cd ${DIR}; ./run-test.sh $2 $3 $4 $5"
}

kill_process(){
	ssh "$1" "cd ${DIR}; ./run-server.sh -k"
}

accuracy_Test(){
	#args
    ITERATIONS=$1
    INSERTFILE=$2
    QUERYFILE=$3
	#test configs
    CONFIGFILE="LDLSH/LDLSH_Quinta"
    RESULTSFOLDER="LDLSH_ACCURACY_TESTS"
	CLIENT="t5.quinta"
	SERVERS="t6.quinta t7.quinta t8.quinta"
	TESTBRANCHNAME="Tests-Accuracy"
	BRANCH="Tests"

    #Setup
	echo "--- Setup client ${CLIENT} ---"
	change_branch $CLIENT $TESTBRANCHNAME
	build "$CLIENT"

	for SERVER in $SERVERS
	do
		echo "--- Setup server ${SERVER} ---"
		change_branch "$SERVER" "$BRANCH"
		build "$SERVER"
	done

    ### LDLSH ###
	RESULTSFOLDER="LDLSH-ACCURACY"
	#start server
    for SERVER in $SERVERS
	do
		echo "LDLDH-Acc: Starting server at ${SERVER}"
		run_server_jar "$SERVER"
	done

	ssh $CLIENT "cd ${DIR}; mkdir ${RESULTSFOLDER}"

	#Insert
	echo "LDLDH-Acc: Runing Inserts..."
    run_test_client_jar "$CLIENT" "$CONFIGFILE" "i" "$INSERTFILE" "$RESULTSFOLDER"

	#Test
    for IT in $(seq "$ITERATIONS")
    do
		#Query
		echo "LDLDH-Acc: Runing test ${IT} out of ${ITERATIONS}..."
        run_test_client_jar "$CLIENT" "$CONFIGFILE" "q" "$QUERYFILE" "$RESULTSFOLDER"
    done

	#Stop Server
    for SERVER in $SERVERS
	do
		echo "LDLDH-Acc: Stoping server at ${SERVER}..."
		kill_process "$SERVER"
	done

	#Copy results
	mkdir "${RESULTSFOLDER}"
	scp -r "${CLIENT}:${DIR}/${RESULTSFOLDER}" "${RESULTSFOLDER}"
}

### main ###
if ! [ $# -eq 4 ];
then
   exit 1
fi

if [ "$1" = "--accuracy" ];
then
    accuracy_Test "$2" "$3" "$4"
fi