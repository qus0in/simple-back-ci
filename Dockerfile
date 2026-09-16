# 1단계: 빌드 환경 (공식 Gradle & JDK 17 LTS 빌더)
FROM gradle:jdk17 AS builder
WORKDIR /workspace

# 의존성 캐시 최적화: 소스코드 변경 전 Gradle 빌드 스크립트 먼저 복사
COPY build.gradle settings.gradle .
RUN gradle dependencies --no-daemon || true

# 소스코드 복사 및 JAR 패키징
COPY src src
RUN gradle bootJar --no-daemon

# 2단계: 실행 환경 (초경량 Alpine 기반 JRE 17 LTS 런타임)
FROM azul/zulu-openjdk-alpine:17-jre-headless
WORKDIR /app

# 빌드 스테이지의 산출물만 복사 (소스코드 및 빌드 도구 제외)
COPY --from=builder /workspace/build/libs/*-SNAPSHOT.jar app.jar

# 비민감 기본 설정만 선언 (DB 접속정보/암호 등 민감한 환경변수는 이미지에 절대 넣지 않음)
ENV PORT=8080

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
