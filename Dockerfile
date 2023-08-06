FROM openjdk:11

WORKDIR /usr/src/app

ARG JAR_PATH=./build/libs

COPY ${JAR_PATH}/IPGeoCheck-0.0.1-SNAPSHOT.jar ${JAR_PATH}/IPGeoCheck-0.0.1-SNAPSHOT.jar

CMD ["java","-jar","./build/libs/IPGeoCheck-0.0.1-SNAPSHOT.jar"]