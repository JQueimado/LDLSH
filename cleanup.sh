HOSTS="t5.quinta t6.quinta t7.quinta t8.quinta t9.quinta"

for HOST in $HOSTS
do
    echo "--- ${HOST} ---"
    ssh $HOST "systemctl stop apache2.service"
    ssh $HOST "systemctl stop docker.service"
    ssh $HOST "systemctl stop redis.service"
done