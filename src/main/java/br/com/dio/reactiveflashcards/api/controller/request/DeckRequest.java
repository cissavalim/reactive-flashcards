package br.com.dio.reactiveflashcards.api.controller.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.util.Set;

public record DeckRequest(@NotBlank
                          @Size(min = 1, max = 255)
                          String name,
                          @NotBlank
                          @Size(min = 1, max = 255)
                          String description,
                          @Valid
                          @NotEmpty
                          @Size(min = 3)
                          Set<CardRequest> cards) {
    
    @Builder(toBuilder = true)
    public DeckRequest {
    }
}
