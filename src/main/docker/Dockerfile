FROM openjdk:8u151-jre-alpine

RUN adduser \
  -h /var/lib/the-hive \
  -u 1000 \
  -D \
  hive hive
RUN apk --update --no-cache add curl

USER hive
WORKDIR /var/lib/the-hive
COPY --chown=hive:hive src/main/docker/*.sh .

ENV SPRING_PROFILES_ACTIVE prod
ARG PORT=8080
EXPOSE ${PORT}
ENTRYPOINT ["sh", "/var/lib/the-hive/docker-entrypoint.sh"]
HEALTHCHECK --timeout=2s --interval=15s \
  CMD sh /var/lib/the-hive/docker-healthcheck.sh

COPY --chown=hive:hive target/the-hive-exec.jar the-hive.jar
