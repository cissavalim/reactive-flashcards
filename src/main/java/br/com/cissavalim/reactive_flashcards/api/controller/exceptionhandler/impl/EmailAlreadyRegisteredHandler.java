package br.com.cissavalim.reactive_flashcards.api.controller.exceptionhandler.impl;

import br.com.cissavalim.reactive_flashcards.api.controller.exceptionhandler.AbstractExceptionHandler;
import br.com.cissavalim.reactive_flashcards.domain.exception.EmailAlreadyRegisteredException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static br.com.cissavalim.reactive_flashcards.domain.exception.BaseErrorMessage.EMAIL_ALREADY_REGISTERED;

@Component
@Order(0)
public class EmailAlreadyRegisteredHandler extends AbstractExceptionHandler {

    public EmailAlreadyRegisteredHandler(final ObjectMapper mapper) {
        super(mapper);
    }

    @Override
    public Mono<Void> handleException(final ServerWebExchange exchange, final Throwable ex) {
        return Mono.fromCallable(() -> {
                    prepareExchange(exchange, HttpStatus.BAD_REQUEST);
                    return ex.getMessage();
                }).map(message -> buildError(HttpStatus.BAD_REQUEST, message))
                .flatMap(response -> writeResponse(exchange, response));
    }

    @Override
    public boolean canHandle(final Throwable ex) {
        return ex instanceof EmailAlreadyRegisteredException;
    }

}
