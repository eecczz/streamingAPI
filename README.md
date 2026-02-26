# AWS S3 & Lambda 기반 동영상 업로드·스트리밍 서비스 (HLS)

대용량 동영상을 업로드하면 AWS 파이프라인을 통해 자동으로 트랜스코딩(HLS)되어,
썸네일과 함께 조회 및 화질별 스트리밍이 가능한 서비스입니다.  
현재는 **Spring Boot 기반 백엔드 구현 중심**이며, 프론트는 React 연동을 전제로 API를 구성했습니다. (배포 전)

---

## 1. 핵심 기능

- **대용량 영상 업로드**
  - AWS **S3**에 영상 파일 업로드
  - 업로드된 영상 메타데이터(DB 저장)

- **자동 트랜스코딩 파이프라인**
  - S3 업로드 이벤트 → **AWS Lambda** 트리거
  - Lambda가 **AWS Elemental MediaConvert** 작업(Job) 생성/실행
  - HLS 형식으로 변환:
    - `.m3u8`(master / variant playlist)
    - `.ts`(HLS chunk) 단위로 분할
  - **화질별(ABR)** 스트리밍을 위해 여러 해상도 변환 지원(예: 360p/720p/1080p)

- **썸네일 생성 및 조회**
  - MediaConvert(또는 별도 설정)로 썸네일 캡처 생성
  - 영상 목록에서 썸네일 + 제목/업로드일 등 조회

- **스트리밍 재생**
  - HLS URL 제공 (React에서 hls.js 또는 video 태그로 재생 가능)
  - 화질 자동/수동 전환 가능 구조

---

## 2. 아키텍처

1) Client(React 예정)에서 영상 업로드 요청  
2) Backend(Spring Boot)가 업로드 정보를 관리하고 S3 업로드를 처리(또는 Presigned URL 방식)  
3) 영상이 S3에 업로드되면 **S3 Event**가 **Lambda** 실행  
4) Lambda가 **MediaConvert** Job을 생성하여 트랜스코딩 시작  
5) 결과물(HLS playlists/chunks/thumbnail)이 Output S3 경로에 저장  
6) Backend는 DB에 저장된 메타데이터 + Output 경로를 기반으로 목록/상세/재생 URL 제공

---

## 3. 기술 스택

### Backend
- Java / Spring Boot
- Spring Web (REST API)
- Spring Data JPA (RDB 연동)
- (선택) Spring Security / JWT (인증이 필요한 경우)

### Frontend (연동 예정)
- React
- HLS Player: `hls.js` (브라우저 HLS 미지원 시)

### AWS
- S3 (원본 영상 업로드 / HLS 결과 저장)
- Lambda (S3 업로드 이벤트 기반 트리거)
- AWS Elemental MediaConvert (HLS 트랜스코딩 및 썸네일 생성)
- IAM (서비스 권한 관리)
- (선택) CloudFront (전송 최적화/캐싱)

---

## 4. 주요 엔드포인트 예시 (Backend)

> 실제 구현에 맞게 경로는 수정하세요.

- `POST /api/videos` : 영상 업로드(또는 업로드 등록)
- `GET /api/videos` : 영상 목록 조회(썸네일 포함)
- `GET /api/videos/{id}` : 영상 상세 조회(메타데이터, 재생 URL)
- `GET /api/videos/{id}/play` : HLS 재생용 m3u8 URL 반환

---

## 5. 환경 변수

-AWS 액세스키 -> 아예 올리지 않았으므로 바로 실행불가!

> MediaConvert는 계정/리전에 따라 Endpoint가 다를 수 있어 별도 설정이 필요합니다.
