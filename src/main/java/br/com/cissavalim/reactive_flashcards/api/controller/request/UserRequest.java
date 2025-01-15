package br.com.cissavalim.reactive_flashcards.api.controller.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

public record UserRequest(
        @NotBlank
        @Size(min = 3, max = 255)
        String name,
        @NotBlank
        @Size(min = 3, max = 255)
        @Email
        String email
) {

    @Builder(toBuilder = true)
    public UserRequest {}
}
