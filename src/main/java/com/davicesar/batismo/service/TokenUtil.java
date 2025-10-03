package com.davicesar.batismo.service; // Ou o seu pacote de utilitários

import java.security.SecureRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Classe utilitária para gerar tokens seguros e legíveis.
 * Esta classe não pode ser instanciada.
 */
public final class TokenUtil {

    // Caracteres escolhidos para evitar confusão (sem I, O, 0, 1).
    private static final String CHARACTERS = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
    private static final int TOKEN_LENGTH = 6;
    private static final SecureRandom random = new SecureRandom();

    /**
     * Construtor privado para impedir a instanciação da classe.
     */
    private TokenUtil() {
    }

    /**
     * Gera um token aleatório com o comprimento e os caracteres definidos.
     * @return Uma String com o token gerado.
     */
    public static String generateToken() {
        return IntStream.range(0, TOKEN_LENGTH)
                .map(i -> random.nextInt(CHARACTERS.length()))
                .mapToObj(CHARACTERS::charAt)
                .map(Object::toString)
                .collect(Collectors.joining());
    }
}