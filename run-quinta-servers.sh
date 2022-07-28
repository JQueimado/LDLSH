# ops:
# host=all 	: apply to all hosts in $HOSTS
# -b 		: build
# -js 		: run server jar
# -jc 		: run client jar
# -p 		: git pull
# -hr 		: hard reset
# -su		: set up

#env
REPOSITORY="https://github.com/JQueimado/Large-scale_distributed_similarity_search_with_Locality-Sensitive_Hashing.git"
BASE_DIR="/root/jqueimado"
DIR="/root/jqueimado/Large-scale_distributed_similarity_search_with_Locality-Sensitive_Hashing"
DIRCONFIG="LDLSH/QS_1000_Demo_quinta"
HOSTS="t5.quinta t6.quinta t7.quinta t8.quinta"

run_server_jar(){
	HOST=$1
	ssh $HOST "cd ${DIR}; java -jar LDLSH-3.2.jar ${DIRCONFIG}/MultimapNode-${HOST}.properties"
}

run_client_jar(){
	HOST=$1
	ssh $HOST "cd ${DIR}; java -jar LDLSH-3.2.jar ${DIRCONFIG}/ClientNode.properties"
}

git_pull(){
	HOST=$1
	ssh $HOST "cd ${DIR}; git pull;"
}

build(){
	HOST=$1
	ssh $HOST "cd ${DIR}; ./package.sh"
}

hard_reset(){
	HOST=$1
	ssh $HOST "rm -rf ${DIR}"
	ssh $HOST "cd ${BASE_DIR}; git clone ${REPOSITORY}"
}

setup_machine(){
	HOST=$1
	ssh $HOST "cd ${DIR}; ./project_setup.sh"
	ssh $HOST "export JAVA_HOME=/usr/lib/jvm/jdk-17"
}

run_once(){
	HOST=$1
	OP=$2
	# run jar 
	if [ $OP = "-js" ]
	then
		run_server_jar $HOST
	fi

	if [ $OP = "-jc" ]
	then
		run_client_jar $HOST
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
}

### MAIN ###
main(){
	HOST=$1
	OP=$2
	if [ $HOST = "all" ]
	then
		for CURRENT_HOST in $HOSTS
		do
			run_once $CURRENT_HOST $OP
		done
	else
		run_once $HOST $OP
	fi
}

if ! [ $# -lt 2 ];
then
	HOST=$1
	OP=$2
else
	exit 1
fi

main $HOST $OP


#SSHCMD="cd ${DIR}; ${CMD}"
#TIMESTAMP=`date +%s`

#echo "Runing ${SSHCMD} in host ${HOST}"

#A=`ssh $HOST "${SSHCMD}"`
#echo $A

#ssh $HOST "ls >>  run_${TIMESTAMP}.log"
