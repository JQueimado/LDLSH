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
	java -server -Xmx100g -XX:+UseG1GC -Dio.netty.leakDetection.level=disabled -jar LDLSH-3.2.jar LDLSH/MemoryTests/LDLSH_Optimized_mem_test/LDLSH_Optimized_mem_test/client_memoryTest2.properties "$1" "$2"
}

kill_process(){
	ssh "$1" "killall -9 java"
	ssh "$1" "cd ${DIR}; rm -rf data"
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
    #start server
    for SERVER in $SERVERS
    do
        echo "$2: Starting server at ${SERVER}"
        run_server_jar "$SERVER" "$CONFIGFILE"
    done

    #Insert
    echo "$2: Runing $TEST Inserts..."
    run_test_client_jar "$CLIENT" "$CONFIGFILE" "i" "$INSERTFILE" "$RESULTSFOLDER" "$TEST"

    kill_process "$CLIENT"

    #Stop Server
    for SERVER in $SERVERS
    do
        echo "$2: Stoping server at ${SERVER}..."
        kill_process "$SERVER"
    done
}

### main ###
# USE
# ./run-test-quinta.sh <Iterations> <Insert_dataset> <Query_Dataset> 
if ! [ $# -eq 3 ];
then
   exit 1
fi

setup_tests

mkdir "TESTS"

run_Test "$1" "MemoryTests" "$2" "$3"