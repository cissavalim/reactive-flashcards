package br.com.cissavalim.reactive_flashcards.api.controller.request;

import br.com.cissavalim.reactive_flashcards.core.validation.MongoId;
import lombok.Builder;

public record StudyRequest(
        @MongoId
        String userId,
        @MongoId
        String deckId
) {
    @Builder(toBuilder = true)
    public StudyRequest {
    }
}
