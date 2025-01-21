package br.com.cissavalim.reactive_flashcards.api.controller.exceptionhandler;

import br.com.cissavalim.reactive_flashcards.domain.exception.NotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class NotFoundHandler extends AbstractExceptionHandler{

    public NotFoundHandler(final ObjectMapper mapper) {
        super(mapper);
    }

    @Override
    Mono<Void> handleException(final ServerWebExchange exchange, final Throwable ex) {
        return Mono.fromCallable(() -> {
                    prepareExchange(exchange, HttpStatus.NOT_FOUND);
                    return ex.getMessage();
                }).map(message -> buildError(HttpStatus.NOT_FOUND, message))
                .flatMap(response -> writeResponse(exchange, response));
    }

    @Override
    boolean canHandle(final Throwable ex) {
        return ex instanceof NotFoundException;
    }
}
