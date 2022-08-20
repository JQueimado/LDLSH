#env
DIR="/root/jqueimado/Large-scale_distributed_similarity_search_with_Locality-Sensitive_Hashing"
DATASETS="${DIR}/data_sets"
CONFIGS="${DIR}/LDLSH/Test_Batery"
CLIENT="t5"
SERVERS="t6 t7 t8"
BRANCH="Tests"
TESTS="accuracy latency throughput"

change_branch(){
	ssh "$1" "cd ${DIR}; git checkout $2"
	pulloutput=$(ssh "$1" "cd ${DIR}; git pull;")
	echo "$pulloutput"
	
	if ! [ "$pulloutput" = "Already up-to-date." ];
	then
		ssh "$1" "cd ${DIR}; ./run-server.sh -b"
	fi
	
	#ssh "$1" "cd ${DIR}; ./run-server.sh -b"
}

run_server_jar(){
 	ssh "$1" "cd ${DIR}; ./run-server.sh -js ${2} "
}

run_test_client_jar(){
	#ssh "$1" "cd ${DIR}; ./run-test.sh $2 $3 $4 $5"
	TIMESTAMP=$(date +%s)
	FILENAME="$5/test_$6_$3_${TIMESTAMP}.txt"
	ssh $1 "cd ${DIR}; java -server -Xmx100g -XX:+UseG1GC -Dio.netty.leakDetection.level=disabled -jar LDLSH-3.2.jar $2/client_$6.properties -$3 $4 >> ${FILENAME}"
}

kill_process(){
	ssh "$1" "cd ${DIR}; ./run-server.sh -k"
}

setup_tests(){
	echo "--- Setup client ${CLIENT} ---"
	change_branch $CLIENT $BRANCH

	for SERVER in $SERVERS
	do
		echo "--- Setup server ${SERVER} ---"
		change_branch "$SERVER" "$BRANCH"
	done
}

run_Test(){
	#args
    ITERATIONS=$1
	CONFIGFILE="${CONFIGS}/$2"
    INSERTFILE="${DATASETS}/$3"
    QUERYFILE="${DATASETS}/$4"
	#test configs
    RESULTSFOLDER="TEST-$2_I-$3_Q-$4_IT-${ITERATIONS}"

    for TEST in $TESTS
	do
		#start server
		for SERVER in $SERVERS
		do
			echo "LDLDH-Acc: Starting server at ${SERVER}"
			run_server_jar "$SERVER" "$CONFIGFILE"
		done

		ssh $CLIENT "cd ${DIR}; mkdir ${RESULTSFOLDER}"

		#Insert
		echo "LDLDH-Acc: Runing $TESTS Inserts..."
		run_test_client_jar "$CLIENT" "$CONFIGFILE" "i" "$INSERTFILE" "$RESULTSFOLDER" "$TEST"

		#Test
		for IT in $(seq "$ITERATIONS")
		do
			#Query
			sleep 1
			echo "LDLDH-Acc: Runing $TESTS test ${IT} out of ${ITERATIONS}..."
			run_test_client_jar "$CLIENT" "$CONFIGFILE" "q" "$QUERYFILE" "$RESULTSFOLDER" "$TEST"
		done

		#Stop Server
		for SERVER in $SERVERS
		do
			echo "LDLDH-Acc: Stoping server at ${SERVER}..."
			kill_process "$SERVER"
		done
	done
	#Copy results
	mkdir "${RESULTSFOLDER}"
	scp -r "${CLIENT}:${DIR}/${RESULTSFOLDER}" "."
	ssh "${CLIENT}" "cd ${DIR}; rm -rf ${RESULTSFOLDER}"
}

### main ###
if ! [ $# -eq 4 ];
then
   exit 1
fi

setup_tests
run_Test "$1" "$2" "$3" "$4"

#if [ "$1" = "--accuracy" ];
#then
#    run_Test "$2" "$3" "$4" "Tests-Accuracy" "LDLSH_Quinta"
#fi

#if [ "$1" = "--latency" ];
#then
#    run_Test "$2" "$3" "$4" "Tests-Latency" "LDLSH_Quinta"
#fi