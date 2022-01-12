package com.tayyab.users.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApplicationException extends RuntimeException {
    private String code;
    private String message;

    public ApplicationException(String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }
}
