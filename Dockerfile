# 1. JDK 기반 이미지
FROM eclipse-temurin:17-jdk

# 2. 작업 디렉토리
WORKDIR /app

# 3. jar 파일 자동 탐색 (어떤 이름이든 상관없게)
COPY build/libs/*.jar app.jar

# 4. 실행
ENTRYPOINT ["java", "-jar", "app.jar"]
