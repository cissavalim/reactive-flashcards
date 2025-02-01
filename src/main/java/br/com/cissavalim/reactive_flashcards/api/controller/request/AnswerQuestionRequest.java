package br.com.cissavalim.reactive_flashcards.api.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

public record AnswerQuestionRequest(
        @NotBlank
        @Size(min = 1, max = 255)
        String answer
) {

    @Builder(toBuilder = true)
    public AnswerQuestionRequest {
    }
}
