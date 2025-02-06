## Gapi Project
<div style="text-align: center;">
    <img src="/docs/구현 사진/메인화면.png" alt="alt text" />
</div>

##### 프로젝트 소개
누구나 쉽고 빠르게 프로젝트를 계획하고 실시간 일정 관리를 통해 팀원 간 협업을 극대화하는 PMS를 개발한다.
##### 사이트 주소: http://34.138.23.151:8080/Project4team_PMS/resources/login.html
<br>
<br>
<br>

## 팀원 
|최진환|김동현|김윤진|박규태|
|:---:|:---:|:---:|:---:|
|<a href="https://github.com/trumanjinhwan" target="_blank"><img src="https://avatars.githubusercontent.com/u/190100768?v=4" height="100px"/><br>trumanjinhwan</a>|<a href="https://github.com/kimdonghyun296" target="_blank"><img src="https://avatars.githubusercontent.com/u/193192616?v=4" height="100px"/><br>kimdonghyun296</a>|<a href="https://github.com/yunndaeng" target="_blank"><img src="https://avatars.githubusercontent.com/u/193191038?v=4" height="100px"/><br>yunndaeng</a>|<a href="https://github.com/ZaRi1l" target="_blank"><img src="https://avatars.githubusercontent.com/u/133009070?v=4" height="100px"/><br>ZaRi1l</a>|

#### 맡은 역할
|이름|업무|
|:---:|---|
|최진환|프론트 엔드: css / 대시보드의 작업 테이블 / 동료 추가 버튼 / 작업 추가와 수정 모달 <br>백엔드: 작업 추가, 삭제, 수정, 조회 / 동료 추가, 삭제, 조회 <br>기타: 서버 연동 / 요구사항 / Usecase Diagram / Class Diagram / DB 설계 / 간트 차트 / 발표 / 발표자료 / 리드미 작성 |
|김동현|프론트 엔드: css / Calendar <br>백엔드: 회원 조회 / 날짜 조회 <br>기타: 발표자료 / 발표 / Sequence Diagram |
|김윤진|프론트 엔드: css / 로그인, 회원가입 화면 <br>백엔드: 회원 추가, 조회 <br>기타: Class Diagram / UI 정의서 |
|박규태|프론트 엔드: css / 로그인, 회원가입 화면 / 대시보드 목록 / 대시보드 추가와 수정 모달 <br>백엔드: 회원 추가, 조회 / 대시보드 추가, 삭제, 수정, 조회 / 동료 추가, 삭제, 조회 <br>기타: 발표자료 / Sequence Diagram / ERD / DB 설계 / SQL문 작성 / 리드미 작성 |

<br><br>
## 개발환경
|OS|Server|IDE|
|:---:|:---:|:---:|
|<img src="https://img.icons8.com/?size=100&id=TuXN3JNUBGOT&format=png&color=000000"/><img src="https://img.icons8.com/?size=100&id=63208&format=png&color=000000"/>|<img src="https://img.icons8.com/?size=100&id=QFcVqyh6lBh6&format=png&color=000000"/>|<img src="https://img.icons8.com/?size=100&id=w1uD6vtDitjr&format=png&color=ffffff"/>|
|Window, Ubuntu|Apache Tomcat|Eclipse|

|Language|DB|VCS|
|:---:|:---:|:---:|
|<img src="https://img.icons8.com/?size=100&id=5OD485koNIrb&format=png&color=000000"/><img src="https://img.icons8.com/?size=100&id=PXTY4q2Sq2lG&format=png&color=000000"/><img src="https://img.icons8.com/?size=100&id=20909&format=png&color=000000"/><img src="https://img.icons8.com/?size=100&id=21278&format=png&color=000000"/>|<img src="https://img.icons8.com/?size=100&id=UFXRpPFebwa2&format=png&color=000000"/>|<img src="https://img.icons8.com/?size=100&id=12599&format=png&color=ffffff"/>|
|Java, Javascript, HTML, CSS|MySQL|GitHub|

#### 개발환경 상세     
|환경|사용|버전|
|:---:|---|---|
|OS| <img src="https://img.shields.io/badge/Ubuntu-E95420?style=for-the-badge&logo=ubuntu&logoColor=white"/>|ubuntu 20.04|
|Server| <img src="https://img.shields.io/badge/apache%20tomcat-%23F8DC75.svg?style=for-the-badge&logo=apache-tomcat&logoColor=black"/>|Apache Tomcat v9.0|
|IDE| <img src="https://img.shields.io/badge/Eclipse-2C2255?style=for-the-badge&logo=eclipse&logoColor=white"/>|Eclipse IDE for Enterprise Java and Web Developers - 2024-09|
|Language| <img src="https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white"/>|Java: JavaSE-17|
|Library| <img src="https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white"/>|javax.activation.jar <br>javax.mail.jar <br>json-simple-1.1.1.jar <br>mysql-connector-java-8.0.22.jar|
|DB| <img src="https://img.shields.io/badge/mysql-4479A1.svg?style=for-the-badge&logo=mysql&logoColor=white"/>|mysql-8.0.41|


<br><br>
## 요구사항

<div style="text-align: center;">
    <img height="500px" src="/docs/소프트웨어 설계/요구사항.png" alt="alt text" />
</div>

#### 요구사항 분석(기능 정리)
|요구사항|상세내용|
|:---:|---|
| 로그인 & 회원가입 | 회원 조회, 추가, 삭제, 수정 |
| 작업 | 회원 조회, 추가, 삭제, 수정 |
| 백로그 | 진행"완료"인 작업들의 모음 |
| 대시보드 | 대시보드 추가, 삭제, 수정 |
| 동료 | 동료 추가, 삭제, 조회 |
| 캘린더 | 작업의 시작일을 캘린더의 표시 |

<br><br>
## 우리 프로젝트

#### 작업
<div style="text-align: center;">
    <img src="/docs/구현 사진/메인화면.png" height="100%" alt="alt text" />
</div> 

<br>

#### 대시보드
<div style="text-align: center;">
    <img src="/docs/구현 사진/대시보드.png" height="100%" alt="alt text" />
</div> 

<br>

#### 캘린더
<div style="text-align: center;">
    <img src="/docs/구현 사진/달력.png" height="100%" alt="alt text" />
</div> 

<br>

<br><br>

## 소프트웨어 설계
<a href="/docs/소프트웨어 설계/README.md" target="_blank">소프트웨어 설계</a>

<br><br>

## PMS 레퍼런스 (Monday.dev)
<a href="/docs/PMS 레퍼런스 사진 모음/README.md" target="_blank">PMS 레퍼런스 (Monday.dev)</a>
