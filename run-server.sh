DIRCONFIG="LDLSH/QS_1000_Demo_quinta"
HOST=`hostname`
nohup java -jar LDLSH-3.2.jar $DIRCONFIG/MultimapNode-$HOST.properties >& nohup.out &
echo $! > pid.nohup
cat pid.nohup