package br.com.cissavalim.reactive_flashcards.domain.exception;

public class EmailAlreadyRegisteredException extends ReactiveFlashcardsException {

    public EmailAlreadyRegisteredException(final String message) {
        super(message);
    }
}
