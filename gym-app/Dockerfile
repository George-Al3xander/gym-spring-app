FROM eclipse-temurin:21-jdk-jammy AS builder
WORKDIR /builder
COPY target/*.jar application.jar
RUN java -Djarmode=tools -jar application.jar \
    extract \
    --layers \
    --destination extracted

FROM eclipse-temurin:21-jre-jammy
WORKDIR /application
COPY --from=builder /builder/extracted/dependencies/ ./
COPY --from=builder /builder/extracted/spring-boot-loader/ ./
COPY --from=builder /builder/extracted/snapshot-dependencies/ ./
COPY --from=builder /builder/extracted/application/ ./

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "application.jar"]
