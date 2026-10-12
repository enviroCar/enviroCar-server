FROM maven:3.10 AS BUILD

WORKDIR /usr/src/app

COPY . /usr/src/app

RUN mvn --batch-mode --errors --fail-fast \
  --define maven.javadoc.skip=true \
  --define skipTests=true install

FROM jetty:9.4.58-jre8

ARG SERVER_VERSION=1.2.0-SNAPSHOT

COPY --from=BUILD /usr/src/app/webapp/target/webapp-${SERVER_VERSION} /var/lib/jetty/webapps/ROOT
COPY ./docker/logback.xml /var/lib/jetty/webapps/ROOT/WEB-INF/classes/

VOLUME /var/data/envirocar/previews

USER root
# FIX CVE
RUN apt-get update \
 && apt-get install -y --no-install-recommends --only-upgrade \
      libssl3t64=3.0.13-0ubuntu3.16 \
      openssl=3.0.13-0ubuntu3.16 \
 && rm -rf /var/lib/apt/lists/*
USER jetty

HEALTHCHECK --interval=5s --timeout=20s --retries=3 \
  CMD wget http://localhost:8080/ -q -O - > /dev/null 2>&1

LABEL maintainer="Christian Autermann <c.autermann@52north.org>" \
      org.label-schema.schema-version="1.0" \
      org.label-schema.name="enviroCar server" \
      org.label-schema.description="enviroCar server" \
      org.label-schema.license="AGPLv3" \
      org.label-schema.url="https://envirocar.org/" \
      org.label-schema.vendor="52°North GmbH" \
      org.label-schema.vcs-url="https://github.com/enviroCar/enviroCar-server.git" \
      org.label-schema.version=${SERVER_VERSION}

