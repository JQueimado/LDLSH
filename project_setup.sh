#java 17
wget https://download.oracle.com/java/17/archive/jdk-17.0.4_linux-x64_bin.deb
sudo dpkg -i jdk-17.0.4_linux-x64_bin.deb
sudo apt-get -f install -y
sudo update-alternatives  --install /usr/bin/java java /usr/lib/jvm/jdk-17/bin/java 1
sudo update-alternatives  --install /usr/bin/javac javac /usr/lib/jvm/jdk-17/bin/javac 1
sudo update-alternatives --set java /usr/lib/jvm/jdk-17/bin/java
sudo update-alternatives --set javac /usr/lib/jvm/jdk-17/bin/javac

#maven
sudo apt-get update
sudo apt-get install -y maven

#cleanup
rm -rf jdk-17.0.4_linux-x64_bin.*

#project
sudo apt-get install git
git clone https://github.com/JQueimado/Large-scale_distributed_similarity_search_with_Locality-Sensitive_Hashing.git
