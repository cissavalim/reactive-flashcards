package br.com.cissavalim.reactive_flashcards.api.controller.response;

import lombok.Builder;

import java.util.Set;

public record DeckResponse(
        String id,
        String name,
        String description,
        Set<CardResponse> cards
) {
    @Builder(toBuilder = true)
    public DeckResponse {
    }
}
