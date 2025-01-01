FROM eclipse-temurin:17-jdk AS build
ARG POM_PROFILE=notset
RUN apt-get update -y \
    && apt-get install -y --no-install-recommends git tzdata \
    && rm -rf /var/lib/apt/lists/*
    
RUN rm /etc/localtime && ln -s /usr/share/zoneinfo/Asia/Kolkata /etc/localtime

ARG MAVEN_VERSION=3.8.7
ARG USER_HOME_DIR="/root"
ARG SHA=332088670d14fa9ff346e6858ca0acca304666596fec86eea89253bd496d3c90deae2be5091be199f48e09d46cec817c6419d5161fb4ee37871503f472765d00
ARG BASE_URL=https://apache.osuosl.org/maven/maven-3/${MAVEN_VERSION}/binaries

RUN mkdir -p /usr/share/maven /usr/share/maven/ref \
  && curl -fsSL -o /tmp/apache-maven.tar.gz https://storage.googleapis.com/public-instape/apache-maven-3.8.8-bin.tar.gz \
  && echo "${SHA}  /tmp/apache-maven.tar.gz" | sha512sum -c - \
  && tar -xzf /tmp/apache-maven.tar.gz -C /usr/share/maven --strip-components=1 \
  && rm -f /tmp/apache-maven.tar.gz \
  && ln -s /usr/share/maven/bin/mvn /usr/bin/mvn

ENV MAVEN_HOME /usr/share/maven
ENV MAVEN_CONFIG "$USER_HOME_DIR/.m2"

COPY src /usr/src/app/src
COPY pom.xml /usr/src/app
RUN mvn -f /usr/src/app/pom.xml clean package -P ${POM_PROFILE} spring-boot:repackage -DskipTests=true

FROM gcr.io/distroless/java17-debian11

COPY --from=build /usr/src/app/target/instape-support-portal-backend-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
