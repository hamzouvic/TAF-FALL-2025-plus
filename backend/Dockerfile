FROM maven:3.9.5-sapmachine-17 as builder
WORKDIR /code
COPY pom.xml .
COPY performance ./performance
COPY backend ./backend
RUN mvn clean install -D maven.test.skip

FROM openjdk:17-jdk-alpine

WORKDIR /opt
COPY apache-jmeter-5.6.3.tgz .

RUN tar -xzf apache-jmeter-5.6.3.tgz
RUN mv apache-jmeter-5.6.3 jmeter
RUN rm apache-jmeter-5.6.3.tgz

WORKDIR /code

ARG JAR_FILE=target/taf-backend-1.0.0-SNAPSHOT.jar

COPY --from=builder /code/backend/${JAR_FILE} /code/app.jar

# Set JMETER_HOME environment variable
ENV JMETER_HOME=/opt/jmeter
ENV PATH=$PATH:$JMETER_HOME/bin
ENV JMETER_INSTALL_DIR=/opt/jmeter

# Create directories for tests and results (optional, but good practice)
RUN mkdir -p /jmeter/tests
RUN mkdir -p /jmeter/results

ENTRYPOINT ["java","-jar","/code/app.jar"]