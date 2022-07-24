cd LDLSH
mvn install:install-file -Dfile=libs/JavaReedSolomon.jar
mvn clean compile install
cd ..
cp LDLSH/target/*.jar .