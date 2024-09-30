FROM openjdk:17-jdk-alpine
EXPOSE 8080
ARG JAR_FILE=build/libs/*-SNAPSHOT.jar
ARG CERT_FILE=src/main/resources/ssl/koreaexim.go.kr.crt
USER root
COPY ${JAR_FILE} app.jar
COPY ${CERT_FILE} /tmp/ca.crt
RUN ln -snf /usr/share/zoneinfo/Asia/Seoul /etc/localtime
RUN keytool -import -trustcacerts -keystore $JAVA_HOME/lib/security/cacerts -storepass changeit -noprompt -alias mycert -file /tmp/ca.crt
ENTRYPOINT ["java", "-jar", "-Duser.timezone=Asia/Seoul", "app.jar"]