package com.spring.security.security.controller.dto;

import jakarta.validation.constraints.NotEmpty;

public record LoginDto(
        @NotEmpty
        String usuario,
        @NotEmpty
        String senha
) {
}
