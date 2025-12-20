package br.com.samuckqadev.farmproject.util;

import java.util.UUID;

public class SellerRegistrationGenerationUtil {

    private static final String PREFIX = "MAT-";

    /**
     * Gera uma matrícula única com o prefixo "MAT-".
     * Exemplo de saída: MAT-8F3D2A1C
     * * @return String contendo a matrícula gerada.
     */
    public static String generate() {
        // Gera um UUID, remove os hífens e pega os primeiros 8 caracteres em caixa alta
        String uniquePart = UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 8)
                .toUpperCase();
                
        return PREFIX + uniquePart;
    }
}