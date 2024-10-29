# 베이스 이미지로 Java 17을 사용
FROM openjdk:17-jdk-slim

# 애플리케이션 JAR 파일을 /app 디렉토리에 복사
WORKDIR /app
COPY build/libs/*.jar app.jar

# 포트 설정 (예: Spring Boot 기본 포트 8080)
EXPOSE 8080

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "app.jar"]
