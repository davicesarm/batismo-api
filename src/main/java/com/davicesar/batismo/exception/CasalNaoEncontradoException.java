package com.davicesar.batismo.exception;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CasalNaoEncontradoException extends RuntimeException{

    public CasalNaoEncontradoException() {
        super("Casal não encontrado.");
    }

    public CasalNaoEncontradoException(Long id) {
        super("Casal com ID " + id + " não pôde ser encontrado.");
    }
}
