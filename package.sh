cd LDLSH
mvn install:install-file -Dfile=libs/JavaReedSolomon.jar -DgroupId=JavaReedSolomon -DartifactId=JavaReedSolomon -Dversion=1.0 -Dpackaging=jar
mvn clean compile install
cd ..
cp LDLSH/target/*.jar .