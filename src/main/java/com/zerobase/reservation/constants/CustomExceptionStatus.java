package com.zerobase.reservation.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
@AllArgsConstructor
public enum CustomExceptionStatus {
    // 에러의 메시지와 코드

    // 회원가입
    ALREADY_EXIST_USERNAME("이미 존재하는 사용자 명입니다.", HttpStatus.BAD_REQUEST.value()),

    // 로그인
    USERNAME_OR_PASSWORD_NOT_MATCH("아이디 또는 비밀번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST.value()),

    // 예약
    ALREADY_APPROVED_RESERVATION("이미 승인된 예약입니다.", HttpStatus.BAD_REQUEST.value()),
    ALREADY_DENIED_RESERVATION("이미 승인거절된 예약입니다.", HttpStatus.BAD_REQUEST.value()),
    ALREADY_VISITED_RESERVATION("이미 방문 완료한 예약입니다.", HttpStatus.BAD_REQUEST.value()),
    RESERVATION_NOT_APPROVED("승인 되지 않은 예약입니다.", HttpStatus.BAD_REQUEST.value()),

    // 리뷰
    ALREADY_WROTE_REVIEW("이미 리뷰 작성이 완료된 예약입니다.", HttpStatus.BAD_REQUEST.value()),
    RESERVATION_NOT_VISITED("아직 방문 완료 하지 않은 예약입니다.", HttpStatus.BAD_REQUEST.value()),

    // 권한 없음
    ACCESS_DENIED("요청 권한이 없습니다.", HttpStatus.BAD_REQUEST.value()),

    // 기타 서비스
    USER_NOT_FOUND("회원을 찾을 수 없습니다.", HttpStatus.BAD_REQUEST.value()),
    STORE_NOT_FOUND("스토어를 찾을 수 없습니다.", HttpStatus.BAD_REQUEST.value()),
    RESERVATION_NOT_FOUND("예약을 찾을 수 없습니다.", HttpStatus.BAD_REQUEST.value()),
    REVIEW_NOT_FOUND("리뷰를 찾을 수 없습니다.", HttpStatus.BAD_REQUEST.value());


    private String message;
    private int code;
}
