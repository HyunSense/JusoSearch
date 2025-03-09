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


## 구현 및 회고
1. **Open API 활용**
    - **도로명 주소 API 연동**: 도로명 주소 검색 기능을 구현하기 위해 [도로명주소 API](https://www.juso.go.kr/)를 활용했습니다. **RestTemplate**을 사용해 API를 호출하고, 응답 데이터를 `String` 형식으로 받아 로그로 출력해보며 API 동작을 확인했습니다. 이후, 응답 데이터를 `JusoResponse` 객체로 변환하여 클라이언트에게 필요한 정보(예: 도로명 주소, 지번 주소, 우편번호 등)만 추출해 반환했습니다.
    - **응답 데이터 파싱**: 응답 데이터를 JusoResponse라는 객체로 변환하려 했는데, 처음엔 JSON 구조를 제대로 맞추지 못해 오류가 났습니다. 그래서 @JsonProperty를 활용해 필드와 JSON 키를 매핑하며 파싱하는 법을 사용하여, Juso 객체로 주소 정보를 뽑아낼 수 있었습니다.
    - **아쉬운점**: `Controller`에서 직접 작성하다 보니 코드가 좀 지저분해졌고, 요청마다 URL을 하드코딩한 게 아쉬웠습니다. 다음엔 별도 서비스 계층으로 분리하거나, API 호출 설정을 깔끔하게 관리하는 방법을 고민해보려 합니다.
2. **소셜 로그인**
    - **OAuth 2.0 로그인구현**: Spring Security의 `oauth2-client`를 활용해 Kakao 로그인을 통합했습니다. 인증 성공 시 **`DefaultOAuth2UserService`를 커스터마이징**해 추가 사용자 정보를 DB에 저장했습니다.
    - **일반 로그인과 통합**:  `PrincipalDetails` 클래스를 통해 **일반 로그인 사용자**와 **소셜 로그인 사용자**를 하나의 클래스로 통합 처리했습니다. `UserDetails`와 `OAuth2User`를 동시에 구현해, 컨트롤러에서 `@AuthenticationPrincipal`로 두 유형의 사용자를 구분 없이 접근할 수 있었습니다. 이를 통해 로그인 방식이 달라도 인증 과정을 통일할 수 있었고, 코드 관리도 한결 쉬워졌습니다.
