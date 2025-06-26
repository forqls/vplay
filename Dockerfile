# 1. 베이스 이미지 선택 (Java 17 버전을 사용)
FROM openjdk:17-jdk-slim

# 2. 작업 폴더 설정
WORKDIR /app

# 3. Gradle 빌드 결과물(.jar 파일)을 복사할 준비
COPY build/libs/*.jar app.jar

# 4. 애플리케이션 실행 (Start Command와 같은 역할)
ENTRYPOINT ["java","-jar","app.jar"]