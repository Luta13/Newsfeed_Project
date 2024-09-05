package org.sparta.newsfeed.common.exception.code;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ErrorCode {

    // 400 Bad Request: 클라이언트의 요청이 잘못되어 서버가 처리할 수 없을 때 발생하는 에러입니다. 주로 파라미터나 요청 형식의 오류에 사용됩니다.
    SELF_FRIEND_REQUEST_NOT_ALLOWED(400,"자신에게 친구 요청을 할 수 없습니다."),
    CANNOT_LIKE_OWN_BOARD(400,"본인의 게시물은 좋아요를 누를 수 없습니다."),
    LIKE_LIMIT_EXCEEDED(400,"좋아요가 이미 눌려진 상태입니다."),
    LIKE_NOT_FOUND(400,"좋아요가 눌러지지 않은 게시물입니다."),
    INVALID_PASSWORD_FORMAT(400,"비밀번호는 대소문자 포함 영문, 숫자, 특수문자를 최소 1글자씩 포함하며, 최소 8글자 이상이어야 합니다."),
    INVALID_EMAIL_OR_PASSWORD(400,"이메일 혹은 비밀번호가 맞지 않습니다."),
    EMAIL_NOT_MATCHED(400,"이메일이 일치하지 않습니다."),
    PASSWORD_NOT_MATCHED(400,"비밀번호가 일치하지 않습니다."),
    CURRENT_PASSWORD_NOT_MATCHED(400,"현재 비밀번호가 일치하지 않습니다."),
    NEW_PASSWORD_SAME_AS_OLD(400,"새로운 비밀번호는 현재 비밀번호와 동일할 수 없습니다."),
    USER_ALREADY_WITHDRAWN(400,"이미 탈퇴한 사용자입니다."),
    ALREADY_LIKED_COMMENT(400,"이미 이 댓글에 좋아요를 눌렀습니다."),
    FRIEND_REQUEST_NOT_FOUND(400,"친구요청을 찾지 못했습니다."),
    LIKE_DOES_NOT_EXIST(400,"좋아요가 존재하지 않습니다."),

    // 403 Forbidden: 서버가 요청을 이해했지만, 클라이언트에게 그 요청을 실행할 권한이 없을 때 발생합니다.
    NOT_A_FRIEND(403,"친구가 아닙니다."),
    AUTHOR_ONLY_CAN_DELETE(403,"작성자만 삭제할 수 있습니다."),
    AUTHOR_ONLY_CAN_EDIT(403,"작성자만 수정할 수 있습니다."),
    COMMENT_AUTHOR_ONLY_CAN_EDIT(403,"작성자 본인만 댓글을 수정할 수 있습니다."),
    COMMENT_AUTHOR_ONLY_CAN_DELETE(403,"작성자 본인만 댓글을 삭제할 수 있습니다."),

    // 404 Not Found: 요청한 리소스를 찾을 수 없을 때 발생합니다.
    BOARD_NOT_FOUND(404, "게시물을 찾을 수 없습니다."),
    USER_NOT_FOUND(404, "유저를 찾을 수 없습니다."),
    COMMENT_NOT_FOUND(404, "댓글을 찾을 수 없습니다."),

    // 409 Conflict: 현재 상태와 충돌하는 요청일 때 사용됩니다. 예를 들어, 중복된 자원이나 이미 존재하는 항목을 생성하려고 할 때 발생합니다.
    ALREADY_FRIEND(409,"이미 친구입니다."),
    ALREADY_FRIEND_REQUESTED(409,"이미 친구 요청을 하였습니다."),
    EMAIL_ALREADY_REGISTERED(409,"이미 등록된 이메일입니다."),

    // 500
    INTERNAL_SERVER_ERROR(500,"서버 에러");

    private final int status;
    private final String message;

    ErrorCode(final int status, final String message) {
        this.status = status;
        this.message = message;
    }

}