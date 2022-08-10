# ops:
# host=all 	: apply to all hosts in $HOSTS
# -b 		: build
# -js 		: run server jar
# -jc 		: run client jar
# -p 		: git pull
# -hr 		: hard reset
# -su		: set up
# -k 		: kill
# --check	: prints used ports
# --change-branch : changes brange in remotes

#env
REPOSITORY="https://github.com/JQueimado/Large-scale_distributed_similarity_search_with_Locality-Sensitive_Hashing.git"
BASE_DIR="/root/jqueimado"
DIR="/root/jqueimado/Large-scale_distributed_similarity_search_with_Locality-Sensitive_Hashing"
DIRCONFIG="Throughtput-test"
HOSTS="t5.quinta t6.quinta t7.quinta t8.quinta"

run_server_jar(){
	HOST=$1
 	ssh $HOST "cd ${DIR}; ./run-server.sh -js"
}

kill_process(){
	HOST=$1
	ssh $HOST "cd ${DIR}; ./run-server.sh -k"
}

run_client_jar(){
	HOST=$1
	ssh $HOST "cd ${DIR}; java -server -Xmx100g -XX:+UseG1GC -jar LDLSH-3.2.jar ${DIRCONFIG}/ClientNode.properties"
}

run_test_client_jar(){
	HOST=$1
	DIRCONFIG=$2
	OP=$3
	DATAFILE=$4
	RESULTSFOLDER=$4
	ssh $HOST "cd ${DIR}; ./run_test.sh ${DIRCONFIG} ${OP} ${DATAFILE} ${RESULTSFOLDER}"
}

git_pull(){
	HOST=$1
	ssh $HOST "cd ${DIR}; git pull;"
}

change_branch(){
	HOST=$1
	BRANCH=$2
	ssh $HOST "cd ${DIR}; git checkout ${BRANCH}"
	git_pull $HOST
	build $HOST
}

status(){
	HOST=$1
	ssh $HOST "cd ${DIR}; git status"
}

build(){
	HOST=$1
	ssh $HOST "cd ${DIR}; ./run-server.sh -b"
}

hard_reset(){
	HOST=$1
	ssh $HOST "rm -rf ${DIR}"
	ssh $HOST "cd ${BASE_DIR}; git clone ${REPOSITORY}"
}

setup_machine(){
	HOST=$1
	ssh $HOST "cd ${DIR}; ./run-server.sh -su"
	ssh $HOST "export JAVA_HOME=/usr/lib/jvm/jdk-17"
}

check_ports(){
	HOST=$1
	ssh $HOST "sudo lsof -i -P -n | grep java"
}

### TESTS ###

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
	BRANCHNAME="Tests-Accuracy"

    #Setup
	change_branch $CLIENT $BRANCHNAME

	for SERVER in $SERVERS
	do
		change_branch $HOST $BRANCH
	done

    ### LDLSH ###
	RESULTSFOLDER="LDLSH-ACCURACY"
	#start server
    for SERVER in $SERVERS
	do
		run_server_jar $SERVER
	done

	ssh $CLIENT "cd ${DIR}; mkdir ${RESULTSFOLDER}"

	#Insert
	echo "LDLDH-Acc: Runing Inserts..."
    run_test_client_jar $CLIENT $CONFIGFILE "-i" $INSERTFILE $RESULTSFOLDER

	#Test
    for IT in {1..$ITERATIONS}
    do
		#Query
		echo "LDLDH-Acc: Runing test ${IT} out of ${ITERATIONS}..."
        run_test_client_jar $CLIENT $CONFIGFILE "-q" $QUERYFILE $RESULTSFOLDER
    done

	#Stop Server
    for SERVER in $SERVERS
	do
		echo "LDLDH-Acc: Stoping server at ${SERVER}..."
		kill_process $SERVER
	done
}


run_once(){
	ARG1=$1
	ARG2=$2
	ARG3=$3
	ARG4=$4
	# run jar 
	if [ $ARG2 = "-js" ]
	then
		run_server_jar $ARG1
	fi

	if [ $ARG2 = "-jc" ]
	then
		run_test_client_jar $ARG1 $ARG3 $ARG4
	fi

	if [ $ARG1 = "-at" ]
	then
		# in this contest
		accuracy_Test $ARG2 $ARG3 $ARG4
	fi

	# pull
	if [ $ARG2 = "-p" ]
	then
		git_pull $ARG1
	fi

	# build
	if [ $ARG2 = "-b" ]
	then
		build $ARG1
	fi

	# hard reset
	if [ $ARG2 = "-hr" ]
	then
		hard_reset $ARG1
	fi

	# setup
	if [ $ARG2 = "-su" ]
	then
		setup_machine $ARG1
	fi

	# kill
	if [ $ARG2 = "-k" ]
	then
		kill_process $ARG1
	fi

	# check
	if [ $ARG2 = "--check" ]
	then
		check_ports $ARG1
	fi

	if [ $ARG2 = "--change-branch" ]
	then
		change_branch $ARG1 $ARG3
	fi

	if [ $ARG2 = "--status" ]
	then
		status $ARG1
	fi

}

### MAIN ###
main(){
	HOST=$1
	OP=$2
	ARG1=$3
	ARG2=$4
	if [ $HOST = "all" ]
	then
		for CURRENT_HOST in $HOSTS
		do
			echo "--- ${CURRENT_HOST} ---"
			run_once $CURRENT_HOST $OP $ARG1 $ARG2
		done
	else
		run_once $HOST $OP $ARG1 $ARG2
	fi
}

if ! [ $# -lt 2 ];
then
	main $1 $2 $3 $4
else
	exit 1
fi

#SSHCMD="cd ${DIR}; ${CMD}"
#TIMESTAMP=`date +%s`

#echo "Runing ${SSHCMD} in host ${HOST}"

#A=`ssh $HOST "${SSHCMD}"`
#echo $A

#ssh $HOST "ls >>  run_${TIMESTAMP}.log"
