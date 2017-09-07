FROM java:8-jre-alpine
MAINTAINER Adrien Lecharpentier <alecharpentier@cloudbees.com>

RUN adduser \
  -h /var/lib/the-hive \
  -u 1000 \
  -D \
  hive hive

WORKDIR /var/lib/the-hive
ADD src/main/docker/docker-entrypoint.sh .
RUN chmod 755 ./docker-entrypoint.sh

ENV SPRING_PROFILES_ACTIVE prod
EXPOSE 8080
ENTRYPOINT ["/var/lib/the-hive/docker-entrypoint.sh"]

USER hive
ADD target/the-hive-exec.jar the-hive.jar
