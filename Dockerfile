# 多阶段构建 - 减小镜像体积
FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /app
COPY pom.xml .
COPY smart-diagnosis-gateway/pom.xml smart-diagnosis-gateway/
COPY smart-diagnosis-auth/pom.xml smart-diagnosis-auth/
COPY smart-diagnosis-service/pom.xml smart-diagnosis-service/
COPY smart-diagnosis-common/pom.xml smart-diagnosis-common/
RUN mkdir -p smart-diagnosis-service/src/main/java && echo "placeholder" > smart-diagnosis-service/src/main/java/placeholder.java

FROM eclipse-temurin:17-jre-alpine
LABEL maintainer="chopinhhm"
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENV JAVA_OPTS="-Xms256m -Xmx512m -XX:+UseG1GC"
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
