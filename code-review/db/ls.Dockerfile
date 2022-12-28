ARG ELK_VERSION
FROM docker.elastic.co/logstash/logstash-oss:${ELK_VERSION}
COPY db_info.conf /conf/db_info.conf