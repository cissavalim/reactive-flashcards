package br.com.cissavalim.reactive_flashcards.api.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

public record CardRequest(
        @NotBlank
        @Size(min = 3, max = 255)
        String front,
        @NotBlank
        @Size(min = 3, max = 255)
        String back
) {
    @Builder(toBuilder = true)
    public CardRequest {
    }
}
