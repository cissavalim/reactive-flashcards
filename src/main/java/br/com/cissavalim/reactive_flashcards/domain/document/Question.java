package br.com.cissavalim.reactive_flashcards.domain.document;

public record Question(
        String asked,
        String answered,
        String expected
) {
}
