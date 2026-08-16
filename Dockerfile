FROM openjdk:22-jdk

COPY target/DockerDataBase-Demo.jar /DockerDataBase-Demo.jar

ENTRYPOINT ["java","-jar","/DockerDataBase-Demo.jar"]