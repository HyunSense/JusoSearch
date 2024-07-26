# 주소검색 서비스 & Security Login
--- ---
# 개발 환경
- Language : Java 11
- Frontend : Javascript, Ajax, html, css(Bootstrap)
- Backend : SpringBoot 2.3, SpringSecurity 5.7, Thymeleaf 3.0, Oauth2-client, MyBatis
- DB : MariaDB
- API : 도로명 주소 검색 API, 네이버 지도 API 
- ETC : VMware, CentOS, IntelliJ

# 프로젝트 주요 기능

## 로그인

### 로그인

- 아이디와 비밀번호를 사용하여 Spring Security를 통한 Form 방식 로그인 가능
- Oauth2 라이브러리를 이용하여 Oauth2 카카오 로그인 가능

### 회원가입

- 사용자 이름(별명),  비밀번호, 이메일 주소로 회원가입 가능
- 이메일 주소는 중복되지 않아야하며, 비밀번호는 안전한 형식(암호화) 되어야 함

## 도로명 주소 검색

### 도로명 주소 검색

- 사용자가 입력한 검색어를 도로명주소API를 통해 주소 검색
- 도로명, 건물번호 및 우편번호로 검색결과 목록을 표시

### 주소 지도 표시 기능

- 네이버 지도 API를 사용하여 검색된 주소를 마커로 표시

## 검색이력 관리 기능

- 사용자는 검색한 주소를 이력관리 테이블에 저장 및 삭제 가능
- 검색목록에서 주소를 클릭시 지도에 마커로 표시

## 가상서버 구축
- Vmware 와 CentOs 를 통하여 MariaDB Database 생성