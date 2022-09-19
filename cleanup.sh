#! /bin/bash
HOSTS="t5 t6 t7 t8 t9"

for HOST in $HOSTS
do
    echo "--- ${HOST} ---"
    ssh $HOST "systemctl stop apache2.service"
    ssh $HOST "systemctl stop docker.service"
    ssh $HOST "systemctl stop redis.service"
done