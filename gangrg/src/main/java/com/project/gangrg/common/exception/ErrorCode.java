package com.project.gangrg.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    NEIGHBORHOOD_NOT_FOUND(404, "해당 동네가 존재하지 않습니다."),
    DUPLICATE_EMAIL(409, "아이디가 중복입니다."),
    DUPLICATE_NICKNAME(409, "닉네임이 중복입니다.");

    private final int status;
    private final String message;
}
