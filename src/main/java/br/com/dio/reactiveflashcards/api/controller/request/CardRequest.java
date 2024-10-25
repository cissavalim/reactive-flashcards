package br.com.dio.reactiveflashcards.api.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

public record CardRequest(@NotBlank
                          @Size(min = 1, max = 255)
                          String front,
                          @NotBlank
                          @Size(min = 1, max = 255)
                          String back) {

    @Builder(toBuilder = true)
    public CardRequest {
    }
}
