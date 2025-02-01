package br.com.cissavalim.reactive_flashcards.api.exceptionhandler.impl;

import br.com.cissavalim.reactive_flashcards.api.exceptionhandler.AbstractExceptionHandler;
import br.com.cissavalim.reactive_flashcards.domain.exception.DeckInStudyException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Order(-1)
public class DeckInStudyHandler extends AbstractExceptionHandler {

    public DeckInStudyHandler(final ObjectMapper mapper) {
        super(mapper);
    }

    @Override
    public Mono<Void> handleException(final ServerWebExchange exchange, final Throwable ex) {
        return Mono.fromCallable(() -> {
                    prepareExchange(exchange, HttpStatus.CONFLICT);
                    return ex.getMessage();
                }).map(message -> buildError(HttpStatus.CONFLICT, message))
                .flatMap(response -> writeResponse(exchange, response));
    }

    @Override
    public boolean canHandle(final Throwable ex) {
        return ex instanceof DeckInStudyException;
    }
}
