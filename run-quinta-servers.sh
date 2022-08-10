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

run_once(){
	# run jar 
	if [ "$2" = "-js" ];
	then
		run_server_jar "$1"
	fi

	if [ "$2" = "-jc" ];
	then
		run_test_client_jar "$1" "$3" "$4"
	fi

	# pull
	if [ "$2" = "-p" ];
	then
		git_pull "$1"
	fi

	# build
	if [ "$2" = "-b" ];
	then
		build "$1"
	fi

	# hard reset
	if [ "$2" = "-hr" ];
	then
		hard_reset "$1"
	fi

	# setup
	if [ "$2" = "-su" ];
	then
		setup_machine "$1"
	fi

	# kill
	if [ "$2" = "-k" ];
	then
		kill_process "$1"
	fi

	# check
	if [ "$2" = "--check" ];
	then
		check_ports "$1"
	fi

	if [ "$2" = "--change-branch" ];
	then
		change_branch "$1" "$3"
	fi

	if [ "$2" = "--status" ];
	then
		status "$1"
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
