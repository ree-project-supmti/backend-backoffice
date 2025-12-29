package com.ree.sireleves.dto.mobile;

public record ChangeSecretCodeRequest(
        String oldSecretCode,
        String newSecretCode
) {}
