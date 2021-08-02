# Board [![Build Status](https://travis-ci.com/oognuyh/board-spring.svg?branch=master)](https://travis-ci.com/oognuyh/board-spring)
<img src="https://img.shields.io/badge/Spring Boot-6DB33F.svg?&style=for-the-badge&logo=SpringBoot&logoColor=white"> <img src="https://img.shields.io/badge/Vue.js-4FC08D.svg?&style=for-the-badge&logo=Vue.js&logoColor=white"> <img src="https://img.shields.io/badge/Vuetify-1867C0.svg?&style=for-the-badge&logo=Vuetify&logoColor=white"> <img src="https://img.shields.io/badge/MariaDB-003545.svg?&style=for-the-badge&logo=MariaDB&logoColor=white"> <img src="https://img.shields.io/badge/Amazon AWS-232F3E.svg?&style=for-the-badge&logo=AmazonAWS&logoColor=white"> <img src="https://img.shields.io/badge/Travis CI-3EAAAF.svg?&style=for-the-badge&logo=TravisCI&logoColor=white"> <img src="https://img.shields.io/badge/Visual Studio Code-007ACC.svg?&style=for-the-badge&logo=VisualStudioCode&logoColor=white">

> 스프링 부트와 뷰 프레임워크를 사용한 Markdown 기반 게시판 프로젝트

Spring Boot, Vue, AWS, CI/CD, Test에 대한 이해를 목표로 진행한 프로젝트 입니다. 
[TOAST UI Editor](https://ui.toast.com/tui-editor)를 활용하여 게시글을 작성하고 조회할 수 있도록 구현하였습니다.
dev 환경에서는 인메모리 데이터베이스를 연동하고 real 환경에서는 Amazon RDS로 생선한 Maria 데이터베이스를 연결하여 작동합니다.

## Features
- 게시판 조회, 추가, 수정, 삭제
- 댓글 조회, 추가, 수정, 삭제
- 로그인/아웃, 회원가입, 회원정보 변경

## Skills
- Backend
    - Java 11, Spring Boot, Spring Data JPA, Spring Security, Spring Validation, JWT, Gradle, Mockito, Junit 5
- Frontend
    - Vue.js, Vuetify, Vuex, Vue-Router, Axios
- Database
    - Amazon RDS for MariaDB
    - In-memory H2
- CI/CD
    - Travis CI, AWS CodeDeploy, EC2, S3, Nginx
- Tool
    - Visual Studio Code

## Systen architecture
![board-architecture](https://user-images.githubusercontent.com/48203569/127770013-c1f85a8e-3fa1-44cc-8cdb-53711f1a433e.png)
Github, EC2, S3, Travis CI, CodeDeploy를 통해 CI/CD 환경을 구성하고 8081과 8082 중 동작하지 않는 포트에 배포 후 Nginx를 통해 reload 하여 서비스 중단 없이 배포가 진행되도록 구현했습니다.
Spring Boot와 Vue.js를 연동하기 위해 build.gradle에 Vue.js가 먼저 빌드 되도록 설정하였습니다.

## ERD
![board-erd](https://user-images.githubusercontent.com/48203569/127770066-06b4a077-ee21-4b66-9d42-e420f2ced50e.png)
기본적으로 created_at, modified_at을 가지고 있으며 이를 @EnableJpaAuditing를 통해 수정이 발생하면 자동적으로 처리하도록 구현했습니다.

## Screen flow
![board-flow](https://user-images.githubusercontent.com/48203569/127769821-6e1fbe65-7dc6-40b5-b8bf-f5d27c8b522d.png)

## Installation and Getting Started
AWS 프리티어를 사용하고 있어 느리지만, http://board.oognuyh.tech 를 통해 접속할 수 있습니다. 
또는,

```
git clone https://github.com/oognuyh/board-spring.git
cd board-spring
./gradlew bootRun --args='--spring.profiles.active=dev'
```

인메모리 데이터베이스와 연결하여 로컬에서 확인할 수 있습니다.
서버가 실행된 후, [localhost:9998](http://localhost:9998) 로 접속하여 확인할 수 있습니다.

## Screens
- /board
![board](https://user-images.githubusercontent.com/48203569/127807402-7b2820f6-0d78-4c34-ab0d-234ffd944770.png)

- /new-post
![new-post-validation](https://user-images.githubusercontent.com/48203569/127806980-0a951a11-a53f-413e-bc90-34abe949e5dc.png)

- /post/{id}
![post-details](https://user-images.githubusercontent.com/48203569/127806982-04577c0b-5308-4a7e-8fa2-aeccd17774df.png)
![post-edit](https://user-images.githubusercontent.com/48203569/127806985-fe47c994-e41f-49f4-83c0-c6c0195bfc65.png)

- /login
![login](https://user-images.githubusercontent.com/48203569/127806976-258720be-4674-464d-81b6-980e0cc6d605.png)

- /signup
![signup-validation](https://user-images.githubusercontent.com/48203569/127807486-17061fc7-60f2-4b7d-9e64-94ac4152b9a5.png)

- /my-page
![my-page](https://user-images.githubusercontent.com/48203569/127806978-ff94db22-1916-4f22-955c-9a0742b3c5c6.png)

## What i learned
- Spring boot
- Vue
- JWT 
- CI/CD

## Library used
- [TOAST UI Editor](https://ui.toast.com/tui-editor)
- [Vuetify](https://vuetifyjs.com)
- [SweetAlert](https://sweetalert.org/)
