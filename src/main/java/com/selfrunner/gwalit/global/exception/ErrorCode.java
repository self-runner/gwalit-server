package com.selfrunner.gwalit.global.exception;

import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@ToString
public enum ErrorCode {

    // Success
    SUCCESS(HttpStatus.OK, 1000, "정상적인 요청입니다."),

    // Common
    INTERNAL_SERVER_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, 2000, "예기치 못한 오류가 발생했습니다."),
    NOT_FOUND_EXCEPTION(HttpStatus.NOT_FOUND, 2001, "존재하지 않는 리소스입니다."),
    INVALID_VALUE_EXCEPTION(HttpStatus.BAD_REQUEST, 2002, "올바르지 않은 요청 값입니다."),
    UNAUTHORIZED_EXCEPTION(HttpStatus.UNAUTHORIZED, 2003, "권한이 없는 요청입니다."),
    ALREADY_DELETE_EXCEPTION(HttpStatus.BAD_REQUEST, 2004, "이미 삭제된 리소스입니다."),
    FAILED_UPLOAD_FILE(HttpStatus.INTERNAL_SERVER_ERROR, 2005, "파일 업로드에 실패했습니다."),
    FAILED_DELETE_FILE(HttpStatus.INTERNAL_SERVER_ERROR, 2006, "파일 삭제에 실패했습니다."),

    // Member
    ALREADY_EXIST_MEMBER(HttpStatus.BAD_REQUEST, 3000, "이미 가입된 정보입니다."),
    WRONG_PASSWORD(HttpStatus.BAD_REQUEST, 3001, "잘못된 비밀번호입니다."),
    SAME_PASSWORD(HttpStatus.BAD_REQUEST, 3002, "기존 비밀번호와 같습니다."),
    WRONG_AUTHENTICATION_CODE(HttpStatus.BAD_REQUEST, 3003, "인증번호가 다릅니다."),
    NOT_MATCH_PASSWORD_RULE(HttpStatus.BAD_REQUEST, 3004, "비밀번호 규칙에 부합하지 않습니다."),
    NOT_EXIST_PHONE(HttpStatus.NOT_FOUND, 3005, "해당 정보와 일치하는 계정이 없습니다."),
    WRONG_TYPE(HttpStatus.NOT_FOUND, 3006, "해당 전화번호는 이 유형으로 가입되지 않았습니다."),
    ALREADY_DELETE_MEMBER(HttpStatus.BAD_REQUEST, 3007, "이미 탈퇴한 계정입니다."),

    // JWT
    WRONG_TOKEN(HttpStatus.UNAUTHORIZED, 4000, "유효하지 않은 토큰입니다."),
    EXPIRE_TOKEN(HttpStatus.UNAUTHORIZED, 4001, "만료된 토큰입니다."),
    LOGOUT_TOKEN(HttpStatus.UNAUTHORIZED, 4002, "로그아웃된 토큰입니다"),


    // Class
    NOT_EXIST_CLASS(HttpStatus.NOT_FOUND, 5000, "존재하지 않는 클래스입니다."),
    NOT_EXIST_LESSON(HttpStatus.NOT_FOUND, 5002, "존재하지 않는 수업입니다."),
    NO_MORE_CLASS(HttpStatus.BAD_REQUEST, 5003, "더 이상 가져올 수업이 없습니다."),
    NOT_EXIST_DEADLINE(HttpStatus.BAD_REQUEST, 5004, "숙제의 기한이 설정되지 않았습니다."),
    FAILED_MAKE_CLASS(HttpStatus.BAD_REQUEST, 5005, "생성할 수 있는 수를 초과했습니다"),
    TOO_MANY_SCHEDULE(HttpStatus.BAD_REQUEST, 5006, "선택된 일정의 수가 너무 많습니다."),

    // Banner
    NO_BANNER_IMAGE(HttpStatus.BAD_REQUEST, 6000, "배너 이미지 파일이 존재하지 않습니다."),
    NO_BANNER_LINK(HttpStatus.BAD_REQUEST, 6001, "배너 링크가 존재하지 않습니다."),

    // Content
    ALREADY_EXIST_CONTENT(HttpStatus.BAD_REQUEST, 7000, "이미 존재하는 콘텐츠입니다."),
    NO_CONTENT_LINK(HttpStatus.BAD_REQUEST, 7001, "콘텐츠 링크가 존재하지 않습니다.");

    private final HttpStatus httpStatus;
    private final Integer code;
    private final String message;

    ErrorCode(HttpStatus httpStatus, Integer code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }
}
