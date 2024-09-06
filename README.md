# Bespoke
> 블로그 플랫폼.

## 배포 주소 및 테스트 계정
- [bespoke 주소](https://beta-bespoke.all-in-one.world/)
- 테스트 계정(id / password)
  - 일반 유저: `user@gmail.com` / `password`
  - 어드민 유저: `admin@gmail.com` / `password`

## 목차
- [개발자 소개](#개발자-소개-및-인사)
- [사용된 기술](#사용된-기술)

# 개발자 소개 및 인사
안녕하세요 남상혁입니다.

백엔드 개발자로의 시작을 알리는 프로젝트로 Bespoke 프로젝트를 시작하게 되었습니다.
많은 블로그 플랫폼이 존재하지만, 각 플랫폼 마다의 장/단점이 보였습니다.

속도가 느린 플랫폼이 있고, 글 쓰기 권한을 제한하는 플랫폼도 있습니다. 광고 때문에 다시 방문하고 싶지 않은 플랫폼도 있습니다.

Bespoke 는
- 빠른 속도를 제공하기 위해 노력합니다.
- 회원가입을 한 유저라면 누구나 글을 작성할 수 있습니다.
- 지속 운영 가능한 서비스를 위해 수익 창출 서비스를 개발중(배너 서비스)에 있습니다.

좋은 서비스 만들기 위해 노력해보겠습니다.

# 사용된 기술

| Type           | Tech                                                                                                                                                                                                                                                                                                                                                                                                                                            | 
|----------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| IDE            | ![IntelliJ IDEA](https://img.shields.io/badge/IntelliJIDEA-000000.svg?style=for-the-badge&logo=intellij-idea&logoColor=white)                                                                                                                                                                                                                                                                                                                   |
| Framework     | ![Spring](https://img.shields.io/badge/SpringBoot_3.3.0-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)                                                                                                                                                                                                                                                                                                                          |
| Backend        | [JPA](https://docs.spring.io/spring-data/jpa/reference/index.html), [Querydsl](http://querydsl.com/), [Thymeleaf](https://www.thymeleaf.org/), [htmx](https://htmx.org/)
| Language       | ![Java](https://img.shields.io/badge/java_JDK17-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)                                                                                                                                                                                                                                                                                                                                 |
| Database       | ![MySQL](https://img.shields.io/badge/mysql_8.0.28-4479A1.svg?style=for-the-badge&logo=mysql&logoColor=white)                                                                                                                                                                                                                                                                                                                                   |
| Cashing        | ![Redis](https://img.shields.io/badge/redis_7.2.5-FF4438?style=for-the-badge&logo=redis&logoColor=white)                                                                                                                                                                                                                                                                                                                                        |
| Message Broker | ![RabbitMQ](https://img.shields.io/badge/Rabbitmq-FF6600?style=for-the-badge&logo=rabbitmq&logoColor=white)                                                                                                                                                                                                                                                                                                                                     |
| Tools          | ![GitHub](https://img.shields.io/badge/github-%23121011.svg?style=for-the-badge&logo=github&logoColor=white) ![Git](https://img.shields.io/badge/git-%23F05033.svg?style=for-the-badge&logo=git&logoColor=white) ![Docker](https://img.shields.io/badge/docker_6.0.16-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white)                                                                                                            |
| Infra          | ![EC2](https://img.shields.io/badge/EC2-FF9900?style=for-the-badge&logo=amazonec2&logoColor=white) ![Route53](https://img.shields.io/badge/Route53-8C4FFF?style=for-the-badge&logo=amazonroute53&logoColor=white) ![Route53](https://img.shields.io/badge/nginx-009639?style=for-the-badge&logo=nginx&logoColor=white)                                                                                                                          |

# 서비스 가이드
Bespoke 에는 3가지 유저가 있습니다. 일반 사용자 / 배너 권한이 부여된 사용자 / 어드민

<details>
모든 일반 유저는 본인의 블로그홈 을 갖게 됩니다.
일반 유저는 <b>카테고리</b>를 생성합니다. 이후 생성하는 게시글에 해당 카테고리를 부여합니다. 카테고리가 부여된 게시글은 <b>게시</b>될 수 있습니다.

아래는 유저의 블로그 페이지 url 규칙 및 해당 페이지에서 액션

1. <code>/blog/{nickname}</code>: 유저의 블로그홈
2. <code>/blog/{nickname}/category/{categoryName}</code>: 유저의 카테고리 페이지.
3. <code>/blog/manage/{entity}</code>: 블로그 관리 페이지
   1. `/blog/manage/profile`: 프로필을 수정
   2. `/blog/manage/posts`: 게시글 관리 / 생성
   3. `/blog/manage/categories`: 카테고리 관리 / 생성
   4. `/blog/manage/banners`: 배너 관리 / 생성. 배너생성 권한이 있어야함. 없는 경우 해당 페이지에서 요청할 수 있음
<summary>
일반 사용자
</summary>
</details>

<details>
배너 권한이 추가 된 사용자는 배너관리 페이지에 접근 시 테이블 과 배너 생성 버튼을 볼 수 있습니다.

배너는 아래의 과정을 통해 게시할 수 있습니다.
1. 배너를 생성한다.
2. 생성 된 배너를 이용해 날짜를 지정해서 게시 요청을 한다.
3. `어드민` 은 게시요청을 어드민 페이지에서 확인 후 `승인` 또는 `거절` 을 한다. `거절`을 할 땐 사유를 작성한다.
4. 유저는 게시가 승인 된 요청이 있을 시 `결제`를 진행한다.
5. 결제가 완료 되면 `스케줄러`에 의해 게시 날짜에 맞춰 자동으로 배너가 게시됩니다.
<summary>
배너 권한 추가 된 사용자
</summary>
</details>
<details>
<code>/admin</code> 페이지에 접근 할 수 있습니다.
User / Post / Banner / Token 을 관리할 수 있습니다. 각 엔터티는 검색 필터와 테이블이 존재합니다.
<summary>
어드민
</summary>
</details>


# Technical Documentation
> 개발중에 있으므로 계속 변경될 수 있습니다.

## ERD
![ERD](https://github.com/user-attachments/assets/6528b1a9-b8e6-446b-ac42-59fabe53a8ca)

## Architecture
![bespoke architecture](https://github.com/user-attachments/assets/40c98391-5192-4922-a256-e411ad77d30d)




