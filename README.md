# Newsfeed_Project

## 📖 프로젝트 소개
### 뉴스피드 프로젝트는 친구들의 가장 최근에 업데이트된 게시물들을 볼 수 있는 페이지를 구현했습니다.

## 📚 기술 스택
Java 17

Spring Boot 3.3.3

Spring Boot Validation

Spring Boot JPA

Mysql

Lombok

JWT

Git

## 📋 API 명세서
### 회원 정보 관리 API
<img width="1188" alt="스크린샷 2024-09-05 오후 4 21 45" src="https://github.com/user-attachments/assets/597ebb4c-e7ac-4cc8-beaa-673f0b75980c">

### 회원가입
경로 : /users/register

메서드 : POST

Request Body

    {
    "email" : "hong@example.com",
    "password" : "password123",
    "name" : "홍길동"
    }

Response Body

    {
    "status": 201,
    "message": "회원가입이 완료되었습니다."
    }

예외 처리

이메일 중복 검증

비밀번호 검증(대소문자 포함 영문 + 숫자 + 특수문자를 최소 1글자씩 포함)

### 회원탈퇴
경로 : /users/unregister

메서드 : DELETE

Request Body

    {
    "email" : "hong@example.com",
    "password" : "password123"
    }

Response Body

    {
    "status": 204,
    "message": "회원탈퇴가 완료되었습니다."
    }

예외 처리

사용자 이메일과 비밀번호 일치 여부

이미 탈퇴한 사용자 아이디 확인

### 로그인
경로 : /users/login

메서드 : POST

Request Body

    {
    "email" : "홍길동",
    "password" : "password123"
    }

Response Body

    {
    "status": 200,
    "message": "로그인에 성공했습니다."
    }

예외 처리

이메일 검증

비밀번호 검증

### 자신의 프로필 조회
경로 : /users/profile

메서드 : GET

Response Body

    {
    "status": 200,
    "message": "프로필 조회했습니다.",
    "body": {
        "email" : "hong@example.com",
        "name" : "홍길동"
      }
    }

### 다른 사용자 프로필 조회
경로 : /users/profile/user?email=이메일

메서드 : GET

Response Body

    {
    "status": 200,
    "message": "프로필 조회했습니다.",
    "body": {
        "name" : "홍길동"
      }
    }

### 비밀번호 변경
경로 : /users/change-password

메서드 : PATCH

Request Body

    {
    "originalPassword" : "originalPassword123",
    "changePassword" : "changePassword123"
    }

Response Body

    {
    "status": 200,
    "message": "비밀번호가 변경되었습니다."
    }

예외 처리

비밀번호 검증(대소문자 포함 영문 + 숫자 + 특수문자를 최소 1글자씩 포함)

### 프로필 수정(이름 수정)
경로 : /users/profile

메서드 : PATCH

Request Body

    {
    "name" : "홍길동2"
    }

Response Body

    {
    "status": 200,
    "message": "프로필이 수정되었습니다."
    }

### 토큰 재발급
경로 : /users/refresh-token

메서드 : POST

Response Body

    {
    "status": 200,
    "message": "토큰 재발급 되었습니다."
    }

## 뉴스피드 API
<img width="1187" alt="스크린샷 2024-09-05 오후 4 22 03" src="https://github.com/user-attachments/assets/9d55b373-0d14-453f-abe6-a500a730bc27">

### 뉴스피드 게시물 조회 (정렬기능 및 기간검색)
경로 : /newsfeed?page=1&sort=&start_dt=&end_dt=

메서드 : GET

Response Body

    {
    "status": 200,
    "message": "조회에 성공했습니다.",
    "body":
    [
      {
        "title" : "게시물 제목",
        "content" : "게시물 내용",
        "userName" : "홍길동",
        "commentCnt" : 0,
        "likeCnt" : 0,
        "createDt" : 2024-09-01 13:00,
        "modifyDt" : 2024-09-02 14:00
      }
      ]
    }

예외 처리

등록된 친구가 아닌 뉴스피드는 볼 수 없게 설정

## 친구 관리 API
<img width="1188" alt="스크린샷 2024-09-05 오후 4 22 23" src="https://github.com/user-attachments/assets/895e7395-60a7-426e-acf2-9abf0bf22ff2">

### 친구 요청
경로 : /friends/request

메서드 : POST

Request Body

    {
      "email" : "test@gmail.com"
    }

Response Body

    {
    "status": 200,
    "message": "친구추가 요청이 완료되었습니다."
    }

예외 처리

친구가 존재하는지 검증

이미 친구인지 검증

### 친구 요청 취소
경로 : /friends/cancel

메서드 : POST

Request Body

    {
      "email" : "test@gmail.com"
    }

Response Body

    {
    "status": 204,
    "message": "친구추가 요청이 반려되었습니다."
    }

### 친구 삭제
경로 : /friends/remove

메서드 : POST

Request Body

    {
      "email" : "test@gmail.com"
    }

Response Body

    {
    "status": 200,
    "message": "친구삭제가 완료되었습니다."
    }

### 친구 요청리스트
경로 : /friends/requests

메서드 : GET

Response Body

    {
    "status": 200,
    "message": "조회에 성공했습니다.",
    "body":
    [
      {
        "name" : "홍길동",
        "createDt" :2024-09-01T13:00:00Z
      }
      ]
    }

### 친구 리스트 
경로 : /friends

메서드 : GET

Response Body

    {
    "status": 200,
    "message": "조회에 성공했습니다.",
    "body":
    [
      {
        "name" : "홍길동",
        "createDt" : 2024-09-01 13:00
        
      }
      ]
    }

### 친구 승인
경로 : /friends/accept

메서드 : POST

Request Body

    {
      "email" : "test@gmail.com"
    }

Response Body

    {
    "status": 200,
    "message": "친구 요청 승인이 완료되었습니다."
    }

예외 처리

요청건이 있는지 검증

### 친구 거절
경로 : /friends/reject

메서드 : POST

Request Body

    {
      "email" : "test@gmail.com"
    }

Response Body

    {
    "status": 200,
    "message": "친구 요청이 거절되었습니다."
    }


## 게시물 관리 API
<img width="1187" alt="스크린샷 2024-09-05 오후 4 22 39" src="https://github.com/user-attachments/assets/25378104-f4ab-4b21-a909-c03a0444e98b">

### 게시물 작성
경로 : /boards

메서드 : POST

Request Body

    {
      "title" : "게시물 제목",
      "content" : "게시물 내용"
    }

Response Body

    {
    "status": 200,
    "massage": "게시물 작성이 완료되었습니다."
    }

### 게시물 조회(작성자 기반)
경로 : /boards?page=1&sort=&start_dt=&end_dt=

메서드 : GET

Response Body

    {
    "status": 200,
    "message": "조회에 성공했습니다.",
    "body":
    [
      {
        "title" : "게시물 제목",
        "content" : "게시물 내용",
        "userName" : "홍길동",
        "commentCnt" : 0,
        "likeCnt" : 0,
        "createDt" : 2024-09-01 13:00,
        "modifyDt" : 2024-09-02 14:00
      }
      ]
    }

### 게시물 수정
경로 : /boards/{boardId}

메서드 : PATCH

Request Body

    {
      "title" : "게시물 제목",
      "content" : "게시물 내용"
    }

Response Body

    {
    "status": 200,
    "message": "게시물 수정이 완료되었습니다."
    }

예외 처리

게시물 작성자 검증 (작성자가 아닌 다른 사용자가 게시물 수정, 삭제를 시도하는 경우)

### 게시물 삭제
경로 : /boards/{iboardId}

메서드 : DELETE

Response Body

    {
    "status": 204,
    "message": "게시물 삭제가 완료되었습니다."
    }

예외 처리

게시물 작성자 검증 (작성자가 아닌 다른 사용자가 게시물 수정, 삭제를 시도하는 경우)

### 게시물 좋아요
경로 : /boards/{board_id}/like

메서드 : POST

Response Body

    {
    "status": 200,
    "message": "좋아요가 등록되었습니다."
    }

예외 처리

게시물이 있는지 검증

### 게시물 좋아요 취소
경로 : /boards/{board_id}/like-cancel

메서드 : DELETE

Response Body

    {
    "status": 200,
    "message": "좋아요가 삭제되었습니다."
    }

예외 처리

게시물이 있는지 검증

## 댓글 관리 API
<img width="1189" alt="스크린샷 2024-09-05 오후 4 22 53" src="https://github.com/user-attachments/assets/1efef57f-2f53-4872-9b14-7b86f930f0ca">
### 댓글 작성
경로 : comments/boards/{board_id}

메서드 : POST

Request Body

    {
      "commentContent" : "댓글 내용"
    }

Response Body

    {
    "status": 200,
    "body": null,
    "message": "댓글이 작성되었습니다."
    }

예외 처리

게시물이 있는지 검증

### 댓글 조회 (게시물 기반)
경로 : comments/boards/{board_id}

메서드 : POST

Response Body

    {
    "status": 200,
    "body":
    [
      {
        "boardTitle" : "게시물제목",
        "commentLikeCount": 1,
        "boardContent" : "게시물내용",
        "commentContent" : "댓글내용",
        "memberName" : "홍길동",
        "createDt" : 2024-09-01 13:00,
        "modifyDt" : 2024-09-02 14:00
      }
      ],
    "message": "조회에 성공했습니다."
    }

예외 처리

게시물이 있는지 검증

### 댓글 수정
경로 : comments/{comment_id}

메서드 : PATCH

Request Body

    {
      "commentContent" : "댓글 내용"
    }

Response Body

    {
    "status": 200,
    "body": null,
    "message": "댓글이 수정되었습니다."
    }

예외 처리

게시물이 있는지 검증

### 댓글 삭제
경로 : comments/{comment_id}

메서드 : DELETE

예외 처리

게시물이 있는지 검증

### 댓글 좋아요
경로 : comments/{comment_id}/like

메서드 : POST

Response Body

    {
    "status": 200,
    "body": null,
    "message": "좋아요가 등록되었습니다."
    }

예외 처리

댓글이 있는지 검증

### 댓글 좋아요 취소
경로 : comments/{comment_id}/like-cancel

메서드 : DELETE

Response Body

    {
    "status": 200,
    "body": null,
    "message": "좋아요가 취소되었습니다."
    }

예외 처리

댓글이 있는지 검증

## 🧾 ERD
<img width="624" alt="스크린샷 2024-09-05 오후 5 41 27" src="https://github.com/user-attachments/assets/4ec1db64-33ea-489a-b8cf-cafeafc47bb1">

---

<img width="733" alt="스크린샷 2024-09-05 오후 5 47 47" src="https://github.com/user-attachments/assets/93ef3be2-b3c0-4e1c-b90b-c907c8a509fa">

https://docs.google.com/spreadsheets/d/13otdcCRqH0BoeUI6rFuDf9rSNH_JqoNk7a3FsK4Qm_E/edit?usp=sharing

