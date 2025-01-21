package br.com.cissavalim.reactive_flashcards.api.controller.exceptionhandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.MethodNotAllowedException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static br.com.cissavalim.reactive_flashcards.domain.exception.BaseErrorMessage.GENERIC_METHOD_NOT_ALLOWED;

@Component
public class MethodNotAllowedHandler extends AbstractExceptionHandler {

    public MethodNotAllowedHandler(final ObjectMapper mapper) {
        super(mapper);
    }

    @Override
    public Mono<Void> handleException(final ServerWebExchange exchange, final Throwable ex) {
        return Mono.fromCallable(() -> {
                    prepareExchange(exchange, HttpStatus.METHOD_NOT_ALLOWED);
                    return GENERIC_METHOD_NOT_ALLOWED.params(exchange.getRequest().getMethod().name()).getMessage();
                }).map(message -> buildError(HttpStatus.METHOD_NOT_ALLOWED, message))
                .flatMap(response -> writeResponse(exchange, response));
    }

    @Override
    boolean canHandle(final Throwable ex) {
        return ex instanceof MethodNotAllowedException;
    }
}
