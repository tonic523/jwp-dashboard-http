# 톰캣 구현하기

## 🚀 1단계 - HTTP 서버 구현하기
### 1. GET /index.html 응답하기

- processor안에 전달받은 socket에서 url 추출하기 `/index.html`  ✅
- 추출한 url로 `static/index.html` 에 접근하기 ✅
- `index.html` 파일의 내용을 response requestBody 값에 담아서 반환하기 ✅

### 2. CSS, javscript 지원하기

- 정적 파일을 조회한다. ✅
  - 정적 페이지가 없을 땐 404.html을 반환한다.
- 파일의 확장자에 맞게 response header의 `Accept`  값을 넣어준다. ✅

### 3. Query String 파싱

- `GET /login` 으로 요청이 왔을 때 login.html 을 응답한다. ✅
- Query String을 파싱한다. ✅
  - Query String 없이 요청이 올 경우 `login.html` 만을 응답한다.
- 파싱된 아이디, 비밀번호와 일치하는 회원을 조회하고 로그로 확인해본다. ✅
  - 올바르지 않은 회원인 경우 `401.html`을 응답한다.

## 🚀 2단계 - 로그인 구현하기
### 1. HTTP Status Code 302

- /login 페이지에서 아이디는 gugu, 비밀번호는 password를 입력한다. ✅
- `GET /login` 요청 시 `login.html`을 응답한다.
- `POST /login` 요청
  - 로그인에 성공하면 응답 헤더에 http status code를 302로 반환하고 `/index.html`로 리다이렉트 한다.
  - 로그인에 실패하면 `401.html`로 리다이렉트한다. ✅

### 2. POST 방식으로 회원가입

- `GET /register` 요청 시 `register.html`을 응답한다. ✅
- `POST /register` 요청
  - 회원가입을 하고 완료하면 `index.html`로 리다이렉트 한다. ✅
  - 회원가입에 실패하면 `400.html`로 리다이렉트 한다. ✅

### 3. Cookie에 JESSIONID 값 저장하기

- 로그인 요청이 오면 Set-Cookie에 JESSIONID 값을 담아 응답한다. ✅
- Cookie 구현 ✅
  - Cookie에 JESSIONID가 없으면 Set-Cookie에 JESSIONID 값을 담아 응답한다.

### 4. Session 구현하기

- 로그인을 한 유저는 JSESSIONID 값을 Session에 저장한다. ✅
- 로그인 요청 시 ✅
  - 로그인 한 유저는 `index.html`로 리다이렉트 한다.
  - 로그인을 하지 않은 유저는 `login.html`로 리다이렉트 한다.

## 🚀 3단계 - 리팩터링
### 1. HttpRequest 클래스 구현하기

- Request Line을 추출할 수 있다. ✅
  - `Method`, `Request Uri`, `Version of the protocol`의 값을 각각 가진다.
  - `Request Uri` 에서 Query String을 추출할 수 있다.
- header를 추출할 수 있다. ✅

### 2. HttpResponse 클래스 구현하기

- Status-Line, Header, body를 가진다. ✅
- Http Response의 규격에 맞는 문자열, 바이트 배열을 반환할 수 있다. ✅

### 3. Controller 인터페이스 추가하기

- `ControllerMapping`: uri에 맞는 컨트롤러를 조회한다.
- `Controller`: `requestLine`에 맞는 메서드를 수행한다.
- `Service`: 비즈니스 로직을 수행한다.
