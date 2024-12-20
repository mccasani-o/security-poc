package com.ccasani.util;

import org.apache.commons.lang3.RandomStringUtils;

public final class CodeGenerator {

    private CodeGenerator(){}

    public static String generateVerificationCode(int length) {
        // Usar el método seguro recomendado
        return RandomStringUtils.secure().nextAlphabetic(length).toUpperCase();
    }
}
