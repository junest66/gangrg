package com.project.gangrg.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    NEIGHBORHOOD_NOT_FOUND(404, "해당 동네가 존재하지 않습니다."),
    DUPLICATE_EMAIL(409, "아이디가 중복입니다."),
    DUPLICATE_NICKNAME(409, "닉네임이 중복입니다."),
    REFRESH_TOKEN_EXPIRED(401, "리프레쉬 토큰이 만료되었습니다."),
    ACCESS_TOKEN_EXPIRED(401, "엑세스 토큰이 만료되었습니다."),
    INVALID_REQUEST(400, "유효하지 않는 요청입니다."),
    LOGIN_FAILURE(401, "로그인에 실패했습니다."),
    SERVER_ERROR(500, "서버에 에러가 발생하였습니다.");

    private final int status;
    private final String message;
}
