# ----- 1단계: 빌드(요리)하는 환경 (자바 21 버전) -----
FROM openjdk:21-jdk-slim AS builder

# 작업 폴더 설정
WORKDIR /app

# 프로젝트 파일 전체 복사
COPY . .

# Gradle 실행 권한 부여 (중요!)
RUN chmod +x ./gradlew

# Gradle로 프로젝트 빌드(요리)하기! (테스트는 건너뛰기!)
RUN ./gradlew build -x test


# ----- 2단계: 실행(포장)하는 환경 (자바 21 버전) -----
FROM openjdk:21-jdk-slim

# 작업 폴더 설정
WORKDIR /app

# 1단계(builder)에서 만들어진 완성품(.jar)만 복사해오기
COPY --from=builder /app/build/libs/*.jar app.jar

# 컨테이너가 시작될 때 실행할 기본 명령어를 설정
CMD ["java", "-jar", "app.jar", "--server.port=${PORT}"]