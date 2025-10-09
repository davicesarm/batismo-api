package com.davicesar.batismo.exception;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class ProcessamentoInvalidoException extends RuntimeException {
    public ProcessamentoInvalidoException(String message) {
        super(message);
    }
}