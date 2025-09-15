package com.davicesar.batismo.dto.login;

public record LoginResponse(
        String accessToken,
        Long expiresIn
) { }
