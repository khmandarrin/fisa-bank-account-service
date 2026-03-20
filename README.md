# fisa-bank-account-service

# Bank Account Service (은행 계좌 서비스)

settlement-service로부터 정산 요청을 받아 은행별 계좌에 입금/차감을 수행하는 서비스입니다.


## 프로젝트 구조

```
src/main/java/dev/bank_account/
├── BankAccountServerApplication.java
├── Account.java                  # 계좌 엔티티
├── BankAccountController.java    # 정산 API 엔드포인트
├── BankAccountService.java       # 정산 비즈니스 로직
├── BankAccountRepository.java    # 계좌 저장소
└── dto/
    ├── DeductRequest.java        # 정산 요청 DTO (은행코드, 금액)
    └── DeductResponse.java       # 정산 응답 DTO (성공여부, 잔액)
```

## 정산 흐름

```
1. settlement-service에서 은행별 정산 금액 리스트를 POST로 전송
2. 각 은행코드로 계좌를 조회
3. netAmount 양수 → 입금(deposit), 음수 → 차감(deduct)
4. 처리 결과(성공여부 + 은행별 잔액)를 응답
```

## API

### POST /api/bok/settle

은행별 정산을 처리합니다.

**Request**
```json
[
  {
    "bankCode": "001",
    "netAmount": -300000.00
  },
  {
    "bankCode": "002",
    "netAmount": 300000.00
  }
]
```

| 필드 | 타입 | 설명 | 검증 |
|------|------|------|------|
| bankCode | String | 은행 코드 | 필수 |
| netAmount | BigDecimal | 정산 금액 (양수: 입금, 음수: 차감) | 필수 |

**Response (200 OK)**
```json
{
  "settled": true,
  "message": "정산 처리 완료",
  "balances": [
    { "bankCode": "001", "balance": 9700000.00 },
    { "bankCode": "002", "balance": 10300000.00 }
  ]
}
```

**Response (실패 시)**
```json
{
  "settled": false,
  "message": "잔액 부족",
  "balances": null
}
```

## DB 스키마

### account 테이블

| 컬럼 | 타입 | 설명 |
|------|------|------|
| id | BIGINT (PK) | 자동 증가 |
| account_no | VARCHAR(20) | 계좌번호 (UNIQUE) |
| bank_code | VARCHAR(3) | 은행 코드 |
| balance | DECIMAL(15,2) | 잔액 |

### 초기 데이터

```sql
INSERT INTO account (account_no, bank_code, balance) VALUES ('110-234-567890', '001', 10000000.00);
INSERT INTO account (account_no, bank_code, balance) VALUES ('220-345-678901', '002', 10000000.00);
```

## 실행 방법

### 1. MySQL 데이터베이스 생성

```sql
CREATE DATABASE bank_account_db;
```

### 2. application.yaml 설정

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/bank_account_db?useSSL=false&serverTimezone=Asia/Seoul
    username: ${DB_USERNAME:root}
    password: ${DB_PASSWORD:1234}
  jpa:
    hibernate:
      ddl-auto: create

server:
  port: 8082

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
```

### 3. 실행

```bash
./gradlew bootRun
```

### 4. Swagger UI 접속

```
http://localhost:8082/swagger-ui.html
```
