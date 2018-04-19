FROM java:8
ADD target/align-0.0.1-SNAPSHOT.jar target/app.jar
RUN bash -c 'touch /app.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-Dspring.profiles.active=container","-jar","target/app.jar"]