package com.davicesar.batismo.exception;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class BatizadoNaoEncontradoException extends RuntimeException {

    public BatizadoNaoEncontradoException() {
        super("O Batizado solicitado não foi encontrado.");
    }

    public BatizadoNaoEncontradoException(Long id) {
        super("Batizado com ID " + id + " não pôde ser encontrado.");
    }

    public BatizadoNaoEncontradoException(String message, Throwable cause) {
        super(message, cause);
    }
}