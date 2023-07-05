FROM openjdk:11-jdk
ARG JAR_FILE=./build/libs/gwalit-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["sh", "-c", "java -Duser.timezone=Asia/Seoul ${JAVA_OPTS} -jar /app.jar"]