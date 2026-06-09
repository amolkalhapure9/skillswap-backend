FROM eclipse-temurin:17
MAINTAINER Amol Kalhapure
WORKDIR /app
COPY target/SkillSwap-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar", "app.jar"]
