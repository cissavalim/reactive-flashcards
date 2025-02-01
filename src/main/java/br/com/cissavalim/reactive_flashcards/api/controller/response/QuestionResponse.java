package br.com.cissavalim.reactive_flashcards.api.controller.response;

import lombok.Builder;

import java.time.OffsetDateTime;

public record QuestionResponse(
        String id,
        String asked,
        OffsetDateTime askedAt
) {
    @Builder(toBuilder = true)
    public QuestionResponse {
    }
}
