package br.com.cissavalim.reactive_flashcards.domain.exception;

public class RetryException extends ReactiveFlashcardsException {

    public RetryException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
