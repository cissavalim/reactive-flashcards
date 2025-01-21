package br.com.cissavalim.reactive_flashcards.api.controller.exceptionhandler.impl;

import br.com.cissavalim.reactive_flashcards.api.controller.exceptionhandler.AbstractExceptionHandler;
import br.com.cissavalim.reactive_flashcards.domain.exception.ReactiveFlashcardsException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static br.com.cissavalim.reactive_flashcards.domain.exception.BaseErrorMessage.GENERIC_EXCEPTION;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ReactiveFlashcardsHandler extends AbstractExceptionHandler {

    public ReactiveFlashcardsHandler(final ObjectMapper mapper) {
        super(mapper);
    }

    @Override
    public Mono<Void> handleException(final ServerWebExchange exchange, final Throwable ex) {
        return Mono.fromCallable(() -> {
                    prepareExchange(exchange, HttpStatus.INTERNAL_SERVER_ERROR);
                    return GENERIC_EXCEPTION.getMessage();
                }).map(message -> buildError(HttpStatus.INTERNAL_SERVER_ERROR, message))
                .flatMap(response -> writeResponse(exchange, response));
    }

    @Override
    public boolean canHandle(final Throwable ex) {
        return ex instanceof ReactiveFlashcardsException;
    }
}
