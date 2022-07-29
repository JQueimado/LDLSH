BASE_DIR="LDLSH"
DIRCONFIG="LDLSH/Throughput"
HOST=`hostname`

run_server(){
    nohup java -server -Xmx100g -XX:+UseG1GC -jar LDLSH-3.2.jar $DIRCONFIG/MultimapNode-$HOST.quinta.properties >& nohup.out &
    echo $! > pid.nohup
    cat pid.nohup
}

build(){
    cd $BASE_DIR
    mvn install:install-file -Dfile=libs/JavaReedSolomon.jar -DgroupId=JavaReedSolomon -DartifactId=JavaReedSolomon -Dversion=1.0 -Dpackaging=jar
    mvn clean install
    cd ..
    cp LDLSH/target/*.jar .
}

kill_process(){
    kill -9 `cat pid.nohup`
    rm pid.nohup nohup.out
}

project_setup(){
    #java 17
    wget https://download.oracle.com/java/17/archive/jdk-17.0.4_linux-x64_bin.deb --no-check-certificate
    sudo dpkg -i jdk-17.0.4_linux-x64_bin.deb
    sudo apt-get -f install -y
    sudo update-alternatives  --install /usr/bin/java java /usr/lib/jvm/jdk-17/bin/java 1
    sudo update-alternatives  --install /usr/bin/javac javac /usr/lib/jvm/jdk-17/bin/javac 1
    sudo update-alternatives --set java /usr/lib/jvm/jdk-17/bin/java
    sudo update-alternatives --set javac /usr/lib/jvm/jdk-17/bin/javac

    #maven
    wget https://dlcdn.apache.org/maven/maven-3/3.8.6/binaries/apache-maven-3.8.6-bin.tar.gz --no-check-certificate
    tar xzvf apache-maven-3.8.6-bin.tar.gz
    sudo mkdir /usr/lib/maven/
    sudo mkdir /usr/lib/maven/apache-maven-3.8.6
    sudo cp -r apache-maven-3.8.6 /usr/lib/maven/.
    sudo chmod a+x /usr/lib/maven/apache-maven-3.8.6/bin/mvn
    sudo update-alternatives --install /usr/bin/mvn mvn /usr/lib/maven/apache-maven-3.8.6/bin/mvn 1
    sudo update-alternatives --set mvn /usr/lib/maven/apache-maven-3.8.6/bin/mvn

    #executables
    sudo chmod +x package.sh

    #cleanup
    rm -rf jdk-17.0.4_linux-x64_bin.*
    rm -rf apache-maven-3.8.6
    rm apache-maven-3.8.6-bin.tar.gz
}

main(){
    OP=$1
    if [ $OP = "-js" ]
    then
        run_server
    fi

    if [ $OP = "-b" ]
    then
        build
    fi

    if [ $OP = "-su" ]
    then
        project_setup
    fi

    if [ $OP = "-k" ]
    then
        kill_process
    fi
}

if [ $# -eq 1 ]
then
    OP=$1
fi

main $OP