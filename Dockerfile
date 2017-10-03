FROM openjdk:8u141-jdk

COPY ./heimdali-api /opt/heimdali-api
COPY ./docker/cloudera.list /etc/apt/sources.list.d/cloudera.list
COPY ./docker/hdfs/* /etc/hadoop/conf.odin/

ADD https://archive.cloudera.com/cdh5/debian/jessie/amd64/cdh/archive.key archive.key

RUN apt-key add archive.key && \
    apt-get update && \
    apt-get install -y -f hadoop-client

RUN update-alternatives --install /etc/hadoop/conf hadoop-conf /etc/hadoop/conf.odin 99

RUN export CLASSPATH="/etc/hadoop/conf.odin/*:`hadoop classpath`"

CMD java -Duser.dir=/opt/heimdali-api -cp `hadoop classpath`:/opt/heimdali-api/conf/:/opt/heimdali-api/lib/* play.core.server.ProdServerStart
