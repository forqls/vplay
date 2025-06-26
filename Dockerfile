# ----- 1단계: 빌드(요리)하는 환경 -----
FROM openjdk:17-jdk-slim AS builder

# 작업 폴더 설정
WORKDIR /app

# 프로젝트 파일 전체 복사
COPY . .

# Gradle 실행 권한 부여 (중요!)
RUN chmod +x ./gradlew

# Gradle로 프로젝트 빌드(요리)하기!
RUN ./gradlew build


# ----- 2단계: 실행(포장)하는 환경 -----
FROM openjdk:17-jdk-slim

# 작업 폴더 설정
WORKDIR /app

# 1단계(builder)에서 만들어진 완성품(.jar)만 복사해오기
COPY --from=builder /app/build/libs/*.jar app.jar

# 애플리케이션 실행
ENTRYPOINT ["java","-jar","app.jar"]