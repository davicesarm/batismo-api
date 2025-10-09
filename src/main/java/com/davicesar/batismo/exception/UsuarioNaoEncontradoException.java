package com.davicesar.batismo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UsuarioNaoEncontradoException extends RuntimeException {

    public UsuarioNaoEncontradoException() {
        super("O usuário solicitado não foi encontrado.");
    }

    public UsuarioNaoEncontradoException(Long id) {
        super("Usuário com ID " + id + " não pôde ser encontrado.");
    }
}
