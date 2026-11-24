# 주문의 달인 백엔드

**주문의 달인**은 키오스크 사용이 익숙하지 않은 어르신이 주문 과정을 반복해서 연습할 수 있도록 만든 시뮬레이션 서비스입니다. 멋쟁이사자처럼 파이널 프로젝트로 개발했습니다.

사용자는 연습 모드와 난이도를 선택하고, 주문 미션에 따라 메뉴와 옵션을 고릅니다. 주문을 마치면 걸린 시간과 정확도를 확인할 수 있습니다. 백엔드는 사용자 정보와 연습 결과를 저장하고, 기록을 바탕으로 월별 통계와 추천 난이도를 제공합니다.

## 주요 기능

| 기능 | 백엔드가 처리하는 내용 |
| --- | --- |
| 회원 및 로그인 | 회원가입 입력값 검증, 아이디 중복 확인, 로그인 및 사용자 정보 조회 |
| 난이도 추천 | 초기 테스트 점수로 초보·중수·달인 중 하나를 추천하고, 현재 추천 난이도를 조회 |
| 연습 기록 | 사용자별 연습 월, 소요 시간(초), 주문 정확도(%) 저장 |
| 마이페이지 | 월별 연습 횟수, 평균 정확도, 평균 소요 시간, 추천 난이도 제공. 해당 월에 연습 기록이 6회 이상이면 평균 정확도와 시간으로 추천 난이도를 다시 계산 |
| AI 채팅 | HTTP와 WebSocket에서 텍스트를 받아 OpenAI API 응답을 반환 |

## 백엔드 개발 및 배포

[김보민](https://github.com/bluebomin)은 회원가입·로그인 API, 공통 예외 처리와 API 문서화, 배포 자동화를 담당했습니다.

- **회원가입·로그인** · [AuthController](ExpertOfOrdering/src/main/java/com/expertofordering/controller/AuthController.java)에서 Bean Validation으로 요청을 검증하고, [AuthService](ExpertOfOrdering/src/main/java/com/expertofordering/service/AuthService.java)에서 아이디 중복과 로그인 실패를 처리합니다.
- **예외 처리와 API 문서** · [GlobalExceptionHandler](ExpertOfOrdering/src/main/java/com/expertofordering/exception/GlobalExceptionHandler.java)에서 입력값 검증 오류와 인증 실패, 중복 요청 등의 응답 형식을 통일합니다. 요청과 응답 스키마는 springdoc-openapi로 Swagger UI에 표시합니다.
- **배포 자동화** · [GitHub Actions 워크플로](.github/workflows/ci-deploy.yml)는 `main` 대상 PR과 `main` 푸시에서 H2 테스트 프로필로 Gradle 테스트 작업을 실행합니다. `main` 푸시에서 해당 작업이 성공하면 실행 JAR를 빌드해 AWS EC2로 전송하고 systemd 서비스를 재시작합니다.

## 주요 API

| 메서드 | 경로 | 기능 |
| --- | --- | --- |
| POST | `/api/auth/signup` | 회원가입 |
| POST | `/api/auth/login` | 로그인 |
| GET | `/api/users/{userId}` | 사용자 정보 조회 |
| POST | `/api/difficulty/initial` | 초기 테스트 점수로 추천 난이도 설정 |
| GET | `/api/difficulty/current?userId={userId}` | 현재 추천 난이도 조회 |
| POST | `/api/practice-logs` | 연습 결과 저장 |
| GET | `/api/mypage?userId={userId}&month={month}` | 월별 연습 요약 조회 |
| POST | `/api/chat` | AI 채팅 응답 요청 |
| WebSocket | `/ws-chat` | 텍스트 메시지로 AI 채팅 |
| GET | `/health` | 서버 상태 확인 |

## 기술 스택

Java 17 · Spring Boot 3.5.7 · Spring Data JPA · MySQL · H2(테스트 프로필) · Gradle Wrapper 8.14.3 · springdoc-openapi · GitHub Actions · AWS EC2

## 로컬 실행

JDK 17과 MySQL을 준비하고, MySQL에 사용할 데이터베이스를 생성합니다. [애플리케이션 설정](ExpertOfOrdering/src/main/resources/application.yml)에서 읽는 환경 변수를 실행할 셸에 설정합니다.

| 환경 변수 | 값 |
| --- | --- |
| `DB_URL` | MySQL JDBC URL. 예시 `jdbc:mysql://localhost:3306/expert_of_ordering` |
| `DB_USERNAME` | MySQL 사용자명 |
| `DB_PASSWORD` | MySQL 비밀번호 |
| `OPENAI_API_KEY` | AI 채팅 호출에 사용하는 OpenAI API 키 |

저장소 루트에서 실행합니다.

```bash
cd ExpertOfOrdering
./gradlew bootRun
```

Windows PowerShell에서는 `ExpertOfOrdering` 디렉터리에서 `.\gradlew.bat bootRun`을 실행합니다.

- Swagger UI는 [localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)에서 확인할 수 있습니다.
- OpenAPI JSON은 [localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)에서 확인할 수 있습니다.
