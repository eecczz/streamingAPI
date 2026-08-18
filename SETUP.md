# 새 클론 실행 설정

## 1. 애플리케이션 설정

저장소 루트에서 `.env.example`을 `.env`로 복사합니다. `.env`는 Git에서 제외됩니다.

| 변수 | 넣을 값 | 비밀 여부 |
| --- | --- | --- |
| `DB_URL` | 로컬/운영 MariaDB JDBC URL | 환경에 따라 민감 |
| `DB_USERNAME` | MariaDB 사용자명 | 민감 |
| `DB_PASSWORD` | MariaDB 비밀번호 | 비밀 |
| `AWS_REGION` | S3 버킷 리전(예: `ap-northeast-2`) | 공개 가능 |
| `AWS_S3_BUCKET` | 업로드에 사용할 S3 버킷 이름 | 환경에 따라 민감 |
| `FRONTEND_URL` | 프런트엔드 주소(기본 `http://localhost:5173`) | 공개 가능 |
| `GOOGLE_CLIENT_ID` | Google OAuth 클라이언트 ID | 환경에 따라 민감 |
| `GOOGLE_CLIENT_SECRET` | Google OAuth 클라이언트 Secret | 비밀 |
| `YOUTUBE_API_KEY` | YouTube Data API 키(선택) | 비밀 |

## 2. AWS 인증

AWS 키는 `.env`나 Spring 설정에 넣지 않습니다. 애플리케이션은 AWS SDK 기본 자격증명 체인을 사용합니다.

- 배포: EC2 인스턴스 프로필 또는 ECS 작업 역할을 연결합니다.
- 로컬 권장: AWS CLI로 별도 프로필을 만든 뒤 실행 셸의 `AWS_PROFILE`에 프로필 이름을 지정합니다.
- 임시 대안: 새로 발급한 `AWS_ACCESS_KEY_ID`, `AWS_SECRET_ACCESS_KEY`, 필요 시 `AWS_SESSION_TOKEN`을 운영체제 환경 변수로만 제공합니다.

노출되었거나 노출이 의심되는 기존 AWS 키는 재사용하지 말고 IAM에서 비활성화·삭제 후 최소 권한으로 새로 발급합니다.

## 3. 실행

Java 17과 MariaDB를 준비한 뒤 백엔드는 Windows에서 `gradlew.bat bootRun`, macOS/Linux에서 `./gradlew bootRun`으로 실행합니다. 프런트엔드는 `frontend` 폴더에서 `npm ci` 후 `npm run dev`를 실행합니다.

실제 값이 든 `.env`, 개인키, AWS 자격증명 파일은 커밋하지 않습니다.
