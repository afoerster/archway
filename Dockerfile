FROM java:8

COPY ./heimdali-api /opt/heimdali-api
COPY ./docker/cloudera.list /etc/apt/sources.list.d/cloudera.list
COPY ./docker/hdfs/* /etc/hadoop/conf.odin/

ADD https://archive.cloudera.com/cdh5/debian/jessie/amd64/cdh/archive.key archive.key

RUN apt-key add archive.key && \
    apt-get update && \
    apt-get install -y -f hadoop-client

RUN update-alternatives --install /etc/hadoop/conf hadoop-conf /etc/hadoop/conf.odin 99

CMD java -Duser.dir=/opt/heimdali-api -cp /opt/heimdali-api/lib/*:/etc/hadoop/conf.odin/:`hadoop classpath`:/opt/heimdali-api/conf/ play.core.server.ProdServerStart
