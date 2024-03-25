# 과릿 - 선생님과 학생이 함께 쓰는 쉬운 수업 관리 서비스
![image](https://github.com/self-runner/gwalit-server/assets/76556999/a25468fb-dc2c-4ea9-8724-8908141aa06b)

## 과릿 사용하기
- [안드로이드 설치하기](https://bit.ly/gwarit-android) 
- [애플 설치하기](https://bit.ly/gwarit-apple)
- [과릿 공지사항](https://wjdwls.notion.site/Gwarit-c2f1540c688a4e85ab132be6992c23cb?pvs=4)
- [과릿 콘텐츠자료실](https://wjdwls.notion.site/5c7069be76044bf2b6ba5464a4841134?pvs=4)
- [과릿 인스타그램](https://www.instagram.com/gwa_rit)

## 개발
### 기술 스택
**Language&Framework**
- Java11/Spring Framework (Spring Boot 2.7.13)
- Spring Data JPA, QueryDsl
- Spring Batch
- Swagger

**Database**
- MySQL
- Redis
  
**Infra**
- AWS Elastic Beanstalk, EC2, AWS RDS, AWS LB, Nginx, AWS S3
- GCP GCE, ALB, GCS, Cloud SQL
- Git, Github Actions

**ETC**
- Slack
- Sentry
- JWT
- Nave SENS API, CoolSMS
- FCM, Jasypt

### 주요 개발 내용
- 스프링 배치를 활용한 매일 오전 9시 지정 알림 발송: 금일의 수업 여부를 알려주기 위해, 스프링 배치를 활용하여 매일 오전 9시 대상 사용자에 한해 FCM 알림 발송 
- Bulk Insert를 활용한 API 성능 개선: JPA의 saveAll의 기존 코드를 JdbcTemplate의 Bulk Insert로 실행 시간 60% 감소 (최대 3초 -> 최대 1.2초)
- 비동기 설정을 통해 FCM 전송 성능 개선: FCM 알림 발송 로직 비동기 처리를 통해 실행 시간 25% 감소 (최대 2초 -> 최대 1.5초) 
- [2시간 주기의 Active Health Check Github Actions, Slack을 활용하여 2시간 주기마다 서버 헬스체크 진행](https://velog.io/@dl-00-e8/%EC%84%9C%EB%B2%84-%ED%97%AC%EC%8A%A4%EC%B2%B4%ED%81%AC-Github-Actions%EB%A5%BC-%ED%99%9C%EC%9A%A9%ED%95%9C-%EC%84%9C%EB%B2%84-%ED%97%AC%EC%8A%A4%EC%B2%B4%ED%81%AC)
- Sentry를 활용한 슬로우 쿼리, 에러 모니터링 AOP를 활용한 슬로우 쿼리 측정 및 서버 에러 발생 시, Sentry 이벤트와 Slack 알림 발송 설정 
- 사용자별 테마 적용을 위한 데이터베이스 설계 사용자가 클래스별 테마를 설정할 수 있도록 참조 테이블을 활용하여 이름/색상 정보 관리
- 운영 환경과 개발 환경 분리 구성(AWS 활용)
- [서버 비용 최소화를 위한 AWS에서 GCP로의 인프라 전환 (다운타임 최소화)](https://velog.io/@dl-00-e8/GCP-%EC%8A%A4%ED%94%84%EB%A7%81%EB%B6%80%ED%8A%B8%EB%A5%BC-%EB%8F%84%EC%BB%A4%EC%97%90-%EB%84%A3%EC%96%B4%EC%84%9C-GCE%EC%97%90-%EC%98%AC%EB%A0%A4%EB%B3%B4%EA%B2%A0%EB%82%98)
- [Naver SENS API의 개인 대상 서비스 종료로 인한, CoolSMS 전환](https://velog.io/@dl-00-e8/%EA%B3%BC%EB%A6%BF-Cool-SMS-%EC%A0%84%ED%99%98%EA%B8%B0)

### ERD
**V3 (2023. 12)**  
![image](https://github.com/self-runner/gwalit-server/assets/76556999/df4ff755-7f50-4b72-ac9d-289dec16064a)  
<details>
<summary>V2 (2023. 11)</summary>
<div markdown="1">
  
![gwalitdb](https://github.com/self-runner/gwalit-server/assets/76556999/4f5639b5-443e-4439-b52d-7a597485b391)  

</div>
</details>
<details>
<summary>V1 (2023. 06)</summary>
<div markdown="1">
  
![image](https://github.com/self-runner/gwalit-server/assets/76556999/787d2997-8022-41b8-8409-765390e5262b)  

</div>
</details>




### Infra Architecture
**V2 (2023. 01 ~ NOW)**   
![gwalit-architecture-gcp 1](https://github.com/self-runner/gwalit-server/assets/76556999/59904463-e604-4a5b-b524-c7e337032f68)  
<details>
<summary>V1 (2023. 06 ~ 2023. 12)</summary>
<div markdown="1">
  
![image](https://github.com/self-runner/gwalit-server/assets/76556999/4c9cccf0-6388-4a83-a3bb-db2470e5bf8b)  

</div>
</details>
