sudo add-apt-repository -y ppa:webupd8team/java
sudo add-apt-repository -y "deb http://ppa.launchpad.net/natecarlson/maven3/ubuntu precise main" 
sudo apt-get update
sudo apt-get install maven3 libmysql-java --force-yes

cd
echo >>.bashrc
echo export PATH=/usr/share/maven3/bin/mvn:$PATH >>.bashrc
echo alias i=\'mvn clean install\' >>.bashrc
source .bashrc