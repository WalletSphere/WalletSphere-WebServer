package com.khomishchak.ws.exceptions.exceptionRules;

import org.springframework.http.HttpStatus;

public interface ExceptionRule {
    Class<? extends RuntimeException> getType();
    HttpStatus getHttpStatus();
}
