# PostgreSQL 설정 가이드

## 개요
이 프로젝트는 Docker를 사용하여 PostgreSQL 데이터베이스를 설정하고 관리합니다.

## 사전 요구사항
- Docker Desktop 설치
- Docker Compose 설치

## 데이터베이스 설정

### 1. PostgreSQL 컨테이너 시작
```bash
docker-compose up -d postgres
```

### 2. 컨테이너 상태 확인
```bash
docker ps
```

### 3. PostgreSQL 로그 확인
```bash
docker logs mcp-postgres
```

## 데이터베이스 연결 정보
- **호스트**: localhost
- **포트**: 5432
- **데이터베이스명**: mcpdb
- **사용자명**: mcpuser
- **비밀번호**: mcppassword

## 데이터베이스 접속 방법

### 1. Docker 컨테이너를 통한 접속
```bash
docker exec -it mcp-postgres psql -U mcpuser -d mcpdb
```

### 2. 외부 클라이언트를 통한 접속
- pgAdmin, DBeaver 등의 클라이언트 사용
- 연결 정보는 위의 데이터베이스 연결 정보 참조

## 초기 데이터
데이터베이스 시작 시 `init-db/01-init.sql` 파일이 자동으로 실행되어 다음이 생성됩니다:
- `design_component` 테이블
- 플로우 차트 컴포넌트 초기 데이터

## 테이블 구조

### design_component 테이블
```sql
CREATE TABLE design_component (
    id UUID PRIMARY KEY,
    description VARCHAR(255) NOT NULL,
    css TEXT NOT NULL,
    html TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

## 유용한 명령어

### 컨테이너 중지
```bash
docker-compose stop postgres
```

### 컨테이너 재시작
```bash
docker-compose restart postgres
```

### 컨테이너 제거 (데이터 유지)
```bash
docker-compose down
```

### 컨테이너 및 볼륨 완전 제거
```bash
docker-compose down -v
```

### 데이터베이스 백업
```bash
docker exec mcp-postgres pg_dump -U mcpuser mcpdb > backup.sql
```

### 데이터베이스 복원
```bash
docker exec -i mcp-postgres psql -U mcpuser -d mcpdb < backup.sql
```

## 문제 해결

### 포트 충돌 시
1. 기존 PostgreSQL 서비스 중지
2. 포트 5432 사용 중인 프로세스 확인:
   ```bash
   netstat -ano | findstr :5432
   ```

### 데이터 초기화
```bash
docker-compose down -v
docker-compose up -d postgres
```

### 로그 확인
```bash
docker logs -f mcp-postgres
```

## Spring Boot 연결 설정
`application.properties` 파일에 다음 설정 추가:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/mcpdb
spring.datasource.username=mcpuser
spring.datasource.password=mcppassword
spring.datasource.driver-class-name=org.postgresql.Driver
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
```