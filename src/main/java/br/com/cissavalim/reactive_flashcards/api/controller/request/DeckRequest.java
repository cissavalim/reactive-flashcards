package br.com.cissavalim.reactive_flashcards.api.controller.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.util.Set;

public record DeckRequest(
        @NotBlank
        @Size(min = 3, max = 255)
        String name,
        @NotBlank
        @Size(min = 3, max = 255)
        String description,
        @Valid
        @Size(min = 3)
        @NotNull
        Set<CardRequest> cards
) {
    @Builder(toBuilder = true)
    public DeckRequest {
    }
}
