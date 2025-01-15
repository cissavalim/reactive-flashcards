package br.com.cissavalim.reactive_flashcards.domain.exception;

public class NotFoundException extends ReactiveFlashcardsException {

    public NotFoundException(final String message) {
        super(message);
    }
}
