package br.com.cissavalim.reactive_flashcards.api.controller.response;

import lombok.Builder;

public record CardResponse(
        String front,
        String back
) {
    @Builder(toBuilder = true)
    public CardResponse {
    }
}
