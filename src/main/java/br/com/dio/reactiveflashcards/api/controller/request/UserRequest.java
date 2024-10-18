package br.com.dio.reactiveflashcards.api.controller.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

public record UserRequest(
        @NotBlank
        @Size(min = 1, max = 255)
        String name,
        @NotBlank
        @Size(min = 1, max = 255)
        @Email
        String email) {

    @Builder(toBuilder = true)
    public UserRequest {
    }
}
