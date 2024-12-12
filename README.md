## 1. 목표와 기능
### 1.1 목표

- JAVA, DB를 바탕으로 실제 백엔드 애플리케이션을 직접 제작한다.
- 손수 구현해보며, 중간중간 기술 바탕이 되는 기본 이론들을 익혀본다.

### 1.2 기능

- 3.요구사항 명세와 기능 명세 항목 참고

## 2. 팀 소개 및 개발 환경 / URL 구조

### 2.1게시판 서비스 B.E팀 구성
#### 역할 분담
- CTO : 박 조은
- BE : 김 현지
- BE : 우 비주
- BE : 김 도현
### 2.1-1 개발 환경
-  Spring Boot 3.4.x
-  MySQL 8.x
-  PostMan


### 2.1-2 URL 구조

- 회원 API

| App  | URL     | HTTP Method | Note | 로그인 권한 필요 |
| ---- | ------- | ----------- | ---- | --------- |
| Auth | /sign   | POST        | 회원가입 |           |
| Auth | /login  | POST        | 로그인  |           |
| Auth | /logout |             | 로그아웃 |           |


- 게시글 API

| App  | URL         | HTTP Method | Note        | 로그인 권한 필요 |
| ---- | ----------- | ----------- | ----------- | :-------: |
| Post | post        | GET         | 게시판 전체 목록   |           |
| Post | post/email  | GET         | 이메일로 목록 필터링 |     ✅     |
| Post | post/write  | POST        | 게시글 작성      |     ✅     |
| Post | post/edit   | UPDATE      | 게시글 수정      |     ✅     |
| Post | post/delete | DELETE      | 게시글 삭제      |     ✅     |


- 댓글 API

| App     | URL             | HTTP Method | Note     | 로그인 권한 필요 |
| ------- | --------------- | ----------- | -------- | :-------: |
| Comment | /comment/all    | GET         | 게시글 댓글목록 |           |
| Comment | /comment/write  | POST        | 댓글 작성    |     ✅     |
| Comment | /comment/edit   | UPDATE      | 댓글 업데이터  |     ✅     |
| Comment | /comment/delete | DELETE      | 댓글 삭제    |     ✅     |

- 좋아요 API

| App  | URL          | HTTP Method | Note         | 로그인 권한 필요 |
| ---- | ------------ | ----------- | ------------ | :-------: |
| Good | /good        | GET         | 좋아요 check 여부 |     ✅     |
| Good | /good/delete | GET         | 좋아요 삭제       |     ✅     |



## 3. 요구사항 및 기능 명세와 예시 데이터 명세

### 게시판 기능

| 기능  | 상세                                                                  | 참고  |
| --- | ------------------------------------------------------------------- | --- |
| 회원  | 1. 이메일 비밀번호 입력하여 회원가입 API 2. 이메일 비밀번호 입력하여 접속하는 API 3. 접속된 유저 로그아웃  |     |
| 조회  | 1. 게시물 전체 조회하는 API 2. 작성자 이메일을 통해 특정 게시물들을 검색하는 API 3. 댓글을 조회하는 API |     |
| 생성  | 1. 댓글을 새롭게 만들 수 있는 API 2. 게시물을 새롭게 만들 수 있는 API                      |     |
| 수정  | 1. 기존 댓글의 글을 수정하는 API 2. 게시물을 새롭게 수정할 수 있는 API                      |     |
| 삭제  | 1. 게시물을 삭제하는 API 2. 댓글을 삭제하는 API                                    |     |
| 옵션  | 1. 이메일과 같이 댓글 좋아요를 할 수 있는 기능                                        |     |

### 게시판 기능 API 인터페이스 스펙

#### 이메일 비밀번호 입력하여 회원가입 API

- HTTP 메소드: POST
- 엔드포인트: /api/signup

요청 본문(JSON 형식):

``` JSON
{ "email": "user@example.com", "password": "user_password" }
```

응답 본문(JSON 형식):

``` JSON
{ "message": "회원가입이 완료되었습니다." }
```
​

#### 이메일 비밀번호 입력하여 접속하는 API

- HTTP 메소드: POST
- 엔드포인트: /api/login

요청 본문(JSON 형식):

```JSON
{ "email": "user@example.com", "password": "user_password"
```
​
응답 본문 HTTP ( token 해더로 )

```SQL
HTTP/1.1 200 OK Content-Type: application/json Authorization: Bearer your_access_token_here { "message": "로그인이 성공적으로 완료되었습니다." }
```


#### 접속된 유저 로그아웃

- HTTP 메소드: POST
- 엔드포인트: /api/logout

요청 본문(JSON 형식):

```JSON
{ "email": "user@example.com" }
```

응답 본문(JSON 형식):

```JSON
{ "message": "로그아웃되었습니다." }
```


#### 게시물 전체 조회하는 API

- HTTP 메소드: GET
- 엔드포인트: /api/posts

응답 본문(JSON 형식):

```JSON
{ "posts": 
	 [
		 { "id": 1, "title": "게시물 제목", "content": "게시물 내용", "author": "작성자", "created_at": "작성일시" },
		 { "id": 2, "title": "게시물 제목", "content": "게시물 내용", "author": "작성자", "created_at": "작성일시" },
	  ... 
	  ]
   }
```


#### 작성자 이메일을 통해 특정 게시물들을 검색하는 API

- HTTP 메소드: GET
- 엔드포인트: /api/posts/search
- 쿼리 파라미터: ?author_email=[user@example.com](mailto:user@example.com)

응답 본문(JSON 형식):

```JSON
{ "posts":
	 [ 
		{ "id": 1, "title": "게시물 제목", "content": "게시물 내용", "author": "작성자", "created_at": "작성일시" },
		{ "id": 3, "title": "게시물 제목", "content": "게시물 내용", "author": "작성자", "created_at": "작성일시" },
	 ... 
	 ]
  }
```
​

#### 댓글을 조회하는 API

- HTTP 메소드: GET
- 엔드포인트: /api/comments

응답 본문(JSON 형식):

```JSON
{ "comments": 
	 [ 
		 { "id": 1, "content": "댓글 내용", "author": "작성자", "post_id": 1, "created_at": "작성일시" },
		 { "id": 2, "content": "댓글 내용", "author": "작성자", "post_id": 1, "created_at": "작성일시" },
	 ... 
	 ]
  }
```


#### 댓글을 새롭게 만들 수 있는 API

- HTTP 메소드: POST
- 엔드포인트: /api/comments

요청 본문(JSON 형식):

```JSON
{ "content": "댓글 내용", "author": "작성자", "post_id": 1 }
```

응답 본문(JSON 형식):

```JSON
{ "message": "댓글이 성공적으로 작성되었습니다." }
```


#### 게시물을 새롭게 만들 수 있는 API

- HTTP 메소드: POST
- 엔드포인트: /api/posts

요청 본문(JSON 형식):

```JSON
{ "title": "게시물 제목", "content": "게시물 내용", "author": "작성자" }
```

응답 본문(JSON 형식):

```JSON
{ "message": "게시물이 성공적으로 작성되었습니다." }
```


#### 기존 댓글의 글을 수정하는 API

- HTTP 메소드: PUT
- 엔드포인트: /api/comments/:comment_id

요청 본문(JSON 형식):

```JSON
{ "content": "수정된 댓글 내용" }
```

응답 본문(JSON 형식):

```JSON
{ "message": "댓글이 성공적으로 수정되었습니다." }
```


#### 게시물을 새롭게 수정할 수 있는 API

- HTTP 메소드: PUT
- 엔드포인트: /api/posts/:post_id
- 요청 본문(JSON 형식):

```JSON
{ "title": "수정된 게시물 제목", "content": "수정 }
```


## 4. 개발 일정(WBS)

![Example Image](src/main/resources/images/image4.png)

요구 사항 분석      :     2024-12-09, 5d
게시글API 개발 - 박조은    :    2024-12-09, 5d
댓글API 개발 - 김현지        :    2024-12-09, 5d
좋아요API 개발 - 우비주    :    2024-12-09, 5d
회원API 개발 - 김도현        :    2024-12-09, 5d

테스트         :     2024-12-13, 1d
문서 작성    :     2024-12-11, 1d


## 5. 화면 설계( U.I )

|                           로그인 폼                           |
|:---------------------------------------------------------:|
|  ![Example Image](src/main/resources/images/image5.png)   |
|                        게시글 전체 목록 폼                        |
|  ![Example Image](src/main/resources/images/image6.png)   |
|                         게시글 작성 폼                          |
|  ![Example Image](src/main/resources/images/image7.png)   |
|                        게시글 상세 내용 폼                        |
|  ![Example Image](src/main/resources/images/image8.png)   |
|                          댓글 작성 폼                          |
|  ![Example Image](src/main/resources/images/image9.png)   |
|                          댓글 내용 폼                          |
| ![Example Image](src/main/resources/images/image_10.png)] |

사진 자료가 없네?

## 6. 데이터베이스 모델링( ERD )

![Example Image](src/main/resources/images/image1.jpg)

## 7. 메인 기능
![Example Image](src/main/resources/images/image3.png)

- 여기에 각자 구현한 기능을 작성

![Example Image](src/main/resources/images/image2.png)

로그인 시 클라이언트가 로그인 요청을 하고, 웹 서버가 로그인 정보를 요구한 후, 서버가 해당 정보를 검증하여 성공 또는 실패 결과를 클라이언트에게 전달하는 과정

## 8. 에러와 에러 해결

## 박 조 은
#### 에러 및 에러 해결
- 여기에 작성하면 됩니다.

## 김 현 지
#### 에러 및 에러 해결
- 여기에 작성하면 됩니다.

## 우 비 주
#### 에러 및 에러 해결
- 여기에 작성하면 됩니다.

## 김 도 현
#### 에러 및 에러 해결
- 원인 : 비동기 통신으로, 서버에서 응답을 받아 데이터를 처리

  -> 컨트롤러에서 뷰가 아닌 해당 데이터를 반환하는 경우 @ResponseBody 어노테이션을 반드시 추가해야 한다 (사실 @RestController 쓰려고 작성하던 것이었을지도..)

- 원인 : HTTP요청의 URL이 비프린터블 ASCII문자를 포함하고 있어서 발생되었다 - 웹 서버나 애플리케이션 서버가 URI의 유효성을 검사할 때, 요청 URI에 포함된 문자가 ASCII 범위를 벗어나거나 인쇄할 수 없는 문자가 포함되어 있음

 ``` Java
.authorizeHttpRequests(authz -> authz
                        .requestMatchers("/").permitAll()
                        .requestMatchers("/api/air-reservation/*").hasAuthority("ROLE_USER")
                        .anyRequest().authenticated()
                )
```

-> WebConfiguration을 설정하는 클래스의 fliterChain메서드에서 아래의 코드를 작성하여 접근 제한을 했기 때문에 팀원들이 기능을 구현한 뒤에 작성한 URL에 맞춰서 재설정

- 원인 : DataSource를 생성할 때 driverClassName이 비어 있다는 것을 의미

-> @ConfigurationProperties(prefix = "spring.datasource")와 @EnableConfigurationProperties(DataSourceProperties.class)
작성이 되어 서로 합이 되는 어노테이션이기에 확인이 필요하고 prefix작성시 yaml의경로를 확인하여 작성이 필요하다.

- Spring Boot의 기본 설정에서 해당 프로퍼티를 인식하지 못하는 것을 의미

-> yaml의 경로를 정확히 확인하여 작성하는 것이 필요

- 원인 : yaml파일이 많은 상황에서 Active Profiles를 지정하지 않거나 지정하지 않은 파일로 설정되어 있어도 문제가 발생할 수 있고 @Repository를 작성하지 않은 Repo클래스가 있는 경우 파일 찾지 못할 때도 문제가 생김

```java
@EnableJpaRepositories(
        basePackages = {"com.github.movieproject.repository.auth.user","com.github.movieproject.repository.auth.role"},
        entityManagerFactoryRef = "entityManagerFactoryBean1",
        transactionManagerRef = "tmJpa1"
)
```

-> copy reference를 통해 basePackage의 파일 경로를 확인하고 @Repository가 설정된 파일을 점검, Active Profies를 확인하여 yaml파일 경로를 확인하는 것도 중요하다.


- JwtProvider의 resolveToken메서드를 반환하는 값이 X-AUTH-TOKEN으로 하고 있었는데 토큰이 일치하지 않아서 동작하지 않았다?

```java
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
//   바꾸기 전 코드 : return request.getHeader("X-AUTH-TOKEN");
    }
```

->Bearer Token으로 교체 작업을 진행함.

- createToken에서 username은 있는데 email 값이 들어오진 않았다.
```java
        Claims claims = Jwts.claims()
                .setSubject(email);
                .setSubject(username);
        claims.put("roles", roles);
```

```JSON
{
          "sub": "acron",
          "roles": [
            "ROLE_USER"
          ],
          "iat": 1733901025,
          "exp": 1733904625
}
```

-> setSubject는 주체를 정하는 것은 한번만 적용이 되고 마지막에 작성한 것을 기준으로 주체를 삼는다. 그래서 아래 코드와 같이 변경했다.
```java
        Claims claims = Jwts.claims()
                .setSubject(email);
        claims.put("username", username);
        claims.put("roles", roles);
```



## 9. 개발하며 느낀점
## 박 조 은
#### 개발하며 느낀점
- 여기에 작성하면 됩니다.

## 김 현 지
#### 개발하며 느낀점
- 여기에 작성하면 됩니다.

## 우 비 주
#### 개발하며 느낀점
- 여기에 작성하면 됩니다.

## 김 도 현
#### 개발하며 느낀점
-  OneToOne과 ManyToOne에서 지정하는 데이터 타입 설정을 몰랐음, repository에서 @Query에서 SQL문 작성 시 Join문에서 동작이 안되었음, OneToOne과 ManyToOne을 사용할 때 작성이 필요한 코드를 몰랐음
   ->테이블 간 관계도와 join구문에 대한 이해가 부족하다. 추가적인 학습이 필요하다.

- Spring Security에서 변경이 가능한 UserDetailsService의 loadByUsername메서드 작성 시 authorities()의 데이터를 처리할 때 문제가 발생하고 해결하는데 까지 상당히 시간이 많이 소요됨
  -> 스트림과 람다식에 대한 이해가 부족하다. 추가적인 학습이 필요하다.

- JwtToken 구현 후 구조가 비슷한 RefreshToken하는 코드를 작성하지 못함, Argument Resolver가 들어가야하는 위치를 찾지 못하고 있음
  -> JWT Token,RefreshToken,Argument Resolver에 대한 이해 및 사용 이유를 제대로 이해하지 못했다. 추가적인 학습이 필요하다.
