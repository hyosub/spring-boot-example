spring-boot-example README.md

### 개발

임효섭 (harryhslim@gmail.com)

### 개발 환경

1. OS
  + Window 10 (64bit)
2. IDE
  + Spring STS (Version: 3.8.3.RELEASE, Build Id: 201612191351)
3. JDK 
  + jdk_1.8.0_111
4. 사용 오픈소스
  + Spring Boot
  + Spring Data with JPA
  + H2 Database

### 실행 환경

1. OS
  + Windows 10 (64bit)
2. WAS
  + Embedded Tomcat
3. Database
  + Embedded H2 Database
4. 실행 Hardware Spec
  + CPU: Intel(R) Core(TM) i7-2720QM CPU @ 2.20GHZ
  + RAM: DDR3 8GB

### 실행 방법

1. JDK가 설치되어 있는 환경에서 'STS IDE'를 설치한다.(https://spring.io/tools)
2. IDE 상에서 Github로부터 프로젝트를 clone 한다.
  + https://github.com/hyosub/spring-pratice/spring-boot-example
3. 자동 빌드가 완료되면 해당 프로젝트를 마우스 우클릭 후 Run As > Spring Boot App 을 실행하여 Run 한다.
4. 서버가 기동된다.
5. 테스트를 위해 Chrome 브라우저의 포스트맨을 실행 후 프로젝트 내부의 포스트맨 collection을 임포트한다.  
  + /spring-boot-pratice/docs/postman/상점 가치 산정 API 개발_v0.4.postman_collection.json
6. collection 임포트가 완료되면 Collections 탭 상에 [상점 가치 산정 API 개발_v0.4] 목록이 생성된다.
7. 생성된 목록을 펼처 [4.1 기타 - 테스트 데이터 생성]의 요청을 전송하여 테스팅 데이터를 생성한다. (약 5분 정도 소요 됨)
8. 테스트 데이터가 모두 생성되면 테스트를 진행한다.

### 개발 수행에 대한 회고

1. 개발 진행 과정
  + 1일차 - Spring Framework 자체는 익숙하고 자주 사용하였으나, Spring Boot 프로젝트와 Spring Data 및 JPA 등은 자주 사용하지 않아서 초반에 많은 공부를 해야 했다.
  + 2일차 - 공부 수행 이틀 후 정도부터 도메인 설계를 시작하였으며, 생각보다 많은 엔티티들이 필요할 것으로 생각되어 머리 속이 혼란스러웠다.
  + 3일차 - 엔티티 설계를 마무리하였고, Spring Boot 프로젝트를 생성하였으며, 머리 속에 설계한 엔티티들을 클래스로 변경하기 시작하였다.
  + 4일차 - ERD 및 기타 클래스 다이어그램 등 도식화하여 설계를 정량화하고 싶어졌으나, 코딩이 시급한 터라 우선 개발부터 쭉 진행하였다.
  + 5일차 - 기본적인 기능은 대부분 실행이 가능해졌으나, JPA 엔티티 도메인을  Jackson의 JSON serialize 과정에서 문제가 발생하기 시작하였다.
  + 6일차 - JPA 엔티티들을 개선하였으며, Jackson 문제도 해결하였다. Transaction rollback 문제 역시 해결하였다.
  + 7일차 - 가치 산정에 성공하였으며, Java Doc을 작성하고 싶었으나, 역시 시간이 문제였다. 문제가 될 만한 오류에 대한Exception 처리에 더 시간을 투자하였다.
2. 결과물 평가
  + 요구사항 기능들은 모두 구현을 하였으나, 사진 정보의 URL 접근 시 실제 사진 다운로드 등 필요한 부가 기능을 만들지 못하였다.
  + JPA 엔티티의 Lazy 페치 전략에 따른 Lazy 로딩을 해결하기 위한 방법으로 Facade를 사용하지 않고 DTO 혹은 VO를 사용하였으나, 근본적인 해결책은 OSIV를 사용하는 것이 가장 좋을 것으로 생각된다.

### 해결해야 하는 이슈 사항

1. Java-doc 추가 기입 필요