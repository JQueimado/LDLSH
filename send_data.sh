DIR="/root/jqueimado/Large-scale_distributed_similarity_search_with_Locality-Sensitive_Hashing"

send_data_sets(){
	HOST=$1
	DATA_DIR=$2
	scp -r $DATA_DIR $HOST:$DIR/.
}

if [ $# -eq 2 ]
then
    send_data_sets $1 $2
fi