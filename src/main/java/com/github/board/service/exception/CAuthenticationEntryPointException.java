package com.github.board.service.exception;


import com.github.board.web.advice.ErrorCode;
import org.springframework.http.HttpStatus;


public class CAuthenticationEntryPointException extends RuntimeException {
    public CAuthenticationEntryPointException(String msg, Throwable t) {
        super(msg, t);
    }

    public CAuthenticationEntryPointException(String msg) {
        super(msg);
    }

    private HttpStatus httpStatus;

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public CAuthenticationEntryPointException(ErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.httpStatus = errorCode.getHttpStatus();
    }

}
