ARG ELK_VERSION
FROM docker.elastic.co/elasticsearch/elasticsearch:${ELK_VERSION}
COPY postgresql-42.5.0.jar /usr/share/logstash/postgresql-42.5.0.jar
RUN elasticsearch-plugin install analysis-nori