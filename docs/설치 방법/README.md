# 설치 방법

#### 버전
|환경|사용|버전|
|:---:|---|---|
|OS| <img src="https://img.shields.io/badge/Ubuntu-E95420?style=for-the-badge&logo=ubuntu&logoColor=white"/>|ubuntu 20.04|
|Server| <img src="https://img.shields.io/badge/apache%20tomcat-%23F8DC75.svg?style=for-the-badge&logo=apache-tomcat&logoColor=black"/>|Apache Tomcat v9.0|
|IDE| <img src="https://img.shields.io/badge/Eclipse-2C2255?style=for-the-badge&logo=eclipse&logoColor=white"/>|Eclipse IDE for Enterprise Java and Web Developers - 2024-09|
|Language| <img src="https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white"/>|JDK: JavaSE-17|
|Library| <img src="https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white"/>|javax.activation.jar <br>javax.mail.jar <br>json-simple-1.1.1.jar <br>mysql-connector-java-8.0.22.jar|
|DB| <img src="https://img.shields.io/badge/mysql-4479A1.svg?style=for-the-badge&logo=mysql&logoColor=white"/>|mysql-8.0.41|

<br><br>

## 로컬 설치 방법
<br>

#### 0. 설치파일    (위에 버전 참고)
- Window os를 준비합니다.
- IDE는 이클립스를 설치합니다.
- 자바 jdk를 다운 받습니다.
- 자바 라이브러리들을 다운 받습니다.
- DB를 설치합니다.

<br>

#### 1. 이클립스
- Eclipse IDE for Enterprise Java and Web Developers 를 실행합니다 <br>
- 자바 jdk를 설정합니다.
- 이클립스에서 임포트를 사용하여 <br> 해당프로젝트(https://github.com/trumanjinhwan/Gapi_Project1_PMS.git) <br>
를 깃 클론 합니다. <br>
- 프로젝트에 자바 라이브러리를 추가합니다.

<br>

#### 2. 톰캣 서버
- 이클립스에 서버를 추가합니다.
- 서버 설정에 들어갑니다.
- Tomcat admin port 는 8005
- HTTP/1.1 은 8080 으로 설정합니다.

<br>

#### 3. DB
- 시스템 사용자로 들어갑니다.
- "apple"라는 사용자를 만들고 권한을 줍니다.
- 해당 사용자로 로그인합니다.
- Gapi_Project1_PMS/docs/DB/2025 02 10/create_table_json with mysel.txt 의 내용을 복사하여 복사 붙여 넣기 합니다.
- Gapi_Project1_PMS/docs/DB/2025 02 10/insert_table_json with mysel.txt 의 내용을 복사하여 복사 붙여 넣기 합니다.

<br>

#### 4. 실행
- 이클립스에서 프로젝트를 실행 버튼을 누릅니다.
- 톰캣 서버를 선택합니다.
- http://localhost:8080/Project4team_PMS/resources/login.html 에 들어갑니다.

