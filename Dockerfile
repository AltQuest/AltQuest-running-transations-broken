FROM debian:jessie

# Default 'httpredir' debian source creates all sorts of crazy problems when your closest mirror is shit
RUN sed -i 's%httpredir.debian.org%ftp.debian.org%' /etc/apt/sources.list
RUN echo "deb http://ppa.launchpad.net/webupd8team/java/ubuntu trusty main" | tee /etc/apt/sources.list.d/webupd8team-java.list
RUN echo "deb-src http://ppa.launchpad.net/webupd8team/java/ubuntu trusty main" | tee -a /etc/apt/sources.list.d/webupd8team-java.list
RUN apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv-keys EEA14886
RUN echo oracle-java8-installer shared/accepted-oracle-license-v1-1 select true | /usr/bin/debconf-set-selections
RUN apt-get update && apt-get install -y oracle-java8-installer oracle-java8-set-default git

RUN mkdir /altquest
COPY . /altquest/
RUN mkdir -p /spigot/plugins

WORKDIR /spigot
RUN wget http://ci.md-5.net/job/NoCheatPlus/lastSuccessfulBuild/artifact/target/NoCheatPlus.jar -O /spigot/plugins/NoCheatPlus.jar

# DOWNLOAD AND BUILD DOWNER
RUN export SHELL=/bin/bash
RUN cd /tmp && git clone https://github.com/bitquest/downer.git
RUN export SHELL=/bin/bash && cd /tmp/downer && ./gradlew setupWorkspace
RUN export SHELL=/bin/bash && cd /tmp/downer && ./gradlew build
RUN cp -rv /tmp/downer/build/libs/*.jar /spigot/plugins

# DOWNLOAD AND BUILD SPIGOT
RUN wget https://hub.spigotmc.org/jenkins/job/BuildTools/lastSuccessfulBuild/artifact/target/BuildTools.jar -O /tmp/BuildTools.jar
RUN export SHELL=/bin/bash && cd /tmp && java -jar BuildTools.jar --rev 1.12
RUN cp /tmp/Spigot/Spigot-Server/target/spigot-1.12-R0.1-SNAPSHOT.jar /spigot/spigot.jar
RUN cd /spigot && echo "eula=true" > eula.txt
COPY server.properties /spigot/
COPY bukkit.yml /spigot/
COPY spigot.yml /spigot/

# Include blockcypher's bcutils
ENV DEBIAN_FRONTEND noninteractive
RUN mkdir /go/
ENV GOPATH /go/
RUN apt-get -y install golang
RUN cd / && git clone https://github.com/blockcypher/btcutils.git
RUN cd / && go get github.com/btcsuite/btcd/btcec
RUN cd /btcutils/signer && go build
RUN chmod +x /btcutils/signer/signer

RUN export SHELL=/bin/bash && cd /altquest/ && ./gradlew setupWorkspace
RUN cd /altquest/ && ./gradlew shadowJar
RUN cp /altquest/build/libs/altquest-1.0-all.jar /spigot/plugins/

CMD java -jar spigot.jar
