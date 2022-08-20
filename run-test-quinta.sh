#!/bin/bash
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
	DATA1=$(cat "${CONFIGFILE}/data_sets.txt")
	read -r -a DATA <<< "${DATA1}"
    INSERTFILE="${DATASETS}/${DATA[0]}"
    QUERYFILE="${DATASETS}/${DATA[1]}"

	#test configs
    RESULTSFOLDER="TEST-$2_I-${DATA[0]}_Q-${DATA[1]}_IT-${ITERATIONS}"
	
	ssh $CLIENT "cd ${DIR}; mkdir ${RESULTSFOLDER}"
    
	for TEST in $TESTS
	do
		#start server
		for SERVER in $SERVERS
		do
			echo "$2: Starting server at ${SERVER}"
			run_server_jar "$SERVER" "$CONFIGFILE"
		done

		#Insert
		echo "$2: Runing $TEST Inserts..."
		run_test_client_jar "$CLIENT" "$CONFIGFILE" "i" "$INSERTFILE" "$RESULTSFOLDER" "$TEST"

		#Test
		for IT in $(seq "$ITERATIONS")
		do
			#Query
			sleep 1
			echo "$2: Runing $TEST test ${IT} out of ${ITERATIONS}..."
			run_test_client_jar "$CLIENT" "$CONFIGFILE" "q" "$QUERYFILE" "$RESULTSFOLDER" "$TEST"
		done

		#Stop Server
		for SERVER in $SERVERS
		do
			echo "$2: Stoping server at ${SERVER}..."
			kill_process "$SERVER"
		done
	done
	#Copy results
	mkdir "TESTS/${RESULTSFOLDER}"
	scp -r "${CLIENT}:${DIR}/${RESULTSFOLDER}" "TESTS/."
	ssh "${CLIENT}" "cd ${DIR}; rm -rf ${RESULTSFOLDER}"
}

### main ###
# USE
# ./run-test-quinta.sh <Iterations> <Insert_dataset> <Query_Dataset> 
if ! [ $# -eq 1 ];
then
   exit 1
fi

TEST_FOLDERS=$(ls LDLSH/Test_Batery)
echo "Found tests: $TEST_FOLDERS"
setup_tests

mkdir "TESTS"

for FOLDER in $TEST_FOLDERS
do
	echo "Runnig test: $FOLDER"
	run_Test "$1" "$FOLDER"
done