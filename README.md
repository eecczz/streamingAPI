# Streaming API

## 프로젝트 개요

대용량 영상 업로드와 스트리밍 처리를 목표로 한 Spring Boot 기반 백엔드 프로젝트입니다. AWS S3, Lambda, MediaConvert를 활용해 업로드된 영상을 HLS 형식으로 변환하고 재생 URL을 제공하는 구조를 설계했습니다.

## 주요 기능

- 영상 업로드 API 설계
- 영상 메타데이터 저장
- AWS S3 업로드 연동
- Lambda 기반 비동기 변환 파이프라인 구상
- HLS 재생 URL 제공 구조 설계

## 기술 스택

- Backend: Java, Spring Boot, Spring Web
- Database: MariaDB
- Cloud: AWS S3, Lambda, MediaConvert
- Streaming: HLS

## 로컬 실행

```bash
./gradlew build
./gradlew bootRun
```

AWS 접근 키, DB 접속 정보 등 민감한 값은 `.env` 또는 로컬 환경 변수로 설정합니다.

## 저장소 관리 기준

- AWS 키와 DB 비밀번호는 Git에 직접 커밋하지 않습니다.
- `.env.example`에는 실제 값이 아닌 placeholder만 남깁니다.
