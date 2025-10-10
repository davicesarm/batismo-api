package com.davicesar.batismo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class OperacaoProibidaException extends RuntimeException{
    public OperacaoProibidaException(String message) {
        super(message);
    }
}
