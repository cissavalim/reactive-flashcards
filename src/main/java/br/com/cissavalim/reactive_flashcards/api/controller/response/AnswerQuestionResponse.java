package br.com.cissavalim.reactive_flashcards.api.controller.response;

import lombok.Builder;

import java.time.OffsetDateTime;

public record AnswerQuestionResponse(
        String asked,
        OffsetDateTime askedAt,
        String answered,
        OffsetDateTime answeredAt,
        String expected
) {

    @Builder(toBuilder = true)
    public AnswerQuestionResponse {
    }
}
