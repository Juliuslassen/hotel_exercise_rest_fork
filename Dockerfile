FROM eclipse-temurin:17-jdk-alpine
# .jar file copied from the target folder of the project
COPY target/app.jar /app.jar
# this is the port that your javalin app will listen too
EXPOSE 7070
# this is the command that will be run when the container starts
ENTRYPOINT ["java", "-jar", "/app.jar"]
