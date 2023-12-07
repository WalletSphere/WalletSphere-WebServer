package com.khomishchak.ws.controllers.handler;

import com.khomishchak.ws.exceptions.exceptionRules.ExceptionRule;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class GlobalErrorAttributes extends DefaultErrorAttributes {

    private final List<ExceptionRule> exceptionRules;

    public GlobalErrorAttributes(List<ExceptionRule> exceptionRules) {
        this.exceptionRules = exceptionRules;
    }

    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
        Throwable error = getError(request);
        Optional<ExceptionRule> exceptionRuleOptional = exceptionRules.stream()
                .filter(exceptionRule -> exceptionRule.getType().isInstance(error))
                .findFirst();

        return exceptionRuleOptional.<Map<String, Object>>map(exceptionRule ->
                        Map.of(ErrorAttributesKey.CODE.getValue(), exceptionRule.getHttpStatus().value(),
                                ErrorAttributesKey.MESSAGE.getValue(), error.getMessage()))
                .orElseGet(() ->
                        Map.of(ErrorAttributesKey.CODE.getValue(), HttpStatus.INTERNAL_SERVER_ERROR,
                                ErrorAttributesKey.MESSAGE.getValue(), error.getMessage()));

    }
}
