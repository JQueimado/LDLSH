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
	OP=$2
	FILE=$3
	ssh $HOST "cd ${DIR}; java -server -Xmx100g -XX:+UseG1GC -jar LDLSH-3.2.jar ${DIRCONFIG}/ClientNode.properties ${OP} ${FILE}"
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

run_once(){
	HOST=$1
	OP=$2
	ARG1=$3
	ARG2=$4
	# run jar 
	if [ $OP = "-js" ]
	then
		run_server_jar $HOST
	fi

	if [ $OP = "-jc" ]
	then
		run_test_client_jar $HOST $ARG1 $ARG2
	fi

	# pull
	if [ $OP = "-p" ]
	then
		git_pull $HOST
	fi

	# build
	if [ $OP = "-b" ]
	then
		build $HOST
	fi

	# hard reset
	if [ $OP = "-hr" ]
	then
		hard_reset $HOST
	fi

	# setup
	if [ $OP = "-su" ]
	then
		setup_machine $HOST
	fi

	# kill
	if [ $OP = "-k" ]
	then
		kill_process $HOST
	fi

	# check
	if [ $OP = "--check" ]
	then
		check_ports $HOST
	fi

	if [ $OP = "--change-branch" ]
	then
		change_branch $HOST $ARG1
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
			run_once $CURRENT_HOST $OP $ARG1
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
