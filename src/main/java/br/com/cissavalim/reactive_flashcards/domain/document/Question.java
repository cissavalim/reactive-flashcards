package br.com.cissavalim.reactive_flashcards.domain.document;

import lombok.Builder;

import java.time.OffsetDateTime;

public record Question(
        String asked,
        OffsetDateTime askedAt,
        String answered,
        OffsetDateTime answeredAt,
        String expected
) {
    @Builder(toBuilder = true)
    public Question {
    }
}
