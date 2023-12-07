package com.khomishchak.ws.exceptions.exceptionRules;

import com.khomishchak.ws.services.integration.whitebit.exceptions.WhiteBitClientException;
import org.springframework.http.HttpStatus;

public class WhiteBitClientExceptionRule implements ExceptionRule {
    @Override
    public Class<? extends RuntimeException> getType() {
        return WhiteBitClientException.class;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }

}
