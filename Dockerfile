FROM ibmcom/ibmjava
MAINTAINER Rockstar Team

VOLUME /tmp
ADD target/coreapi-microservice-1.0.0-SNAPSHOT.jar coreapi-microservice.jar
RUN bash -c 'touch /coreapi-microservice.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/coreapi-microservice.jar"]