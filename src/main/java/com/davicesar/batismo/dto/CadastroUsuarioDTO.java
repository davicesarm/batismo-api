package com.davicesar.batismo.dto;

public record CadastroUsuarioDTO (
    String email,
    String cargo,
    String senha,
    String nome,
    String marido,
    String mulher
) {}
