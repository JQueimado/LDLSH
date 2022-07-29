if [ $# -eq 1 ]
then
	KEY=$1
else
	exit 1
fi
HOSTS="t5.quinta t6.quinta t7.quinta t8.quinta"

for HOST in $HOSTS
do
	ssh $HOST "cd /root/.ssh; echo ${KEY} >> authorized_keys"
done
