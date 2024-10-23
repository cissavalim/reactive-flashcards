package br.com.dio.reactiveflashcards.api.exceptionhandler.handlers;

import br.com.dio.reactiveflashcards.api.exceptionhandler.AbstractExceptionHandler;
import br.com.dio.reactiveflashcards.domain.exception.BaseErrorMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class ResponseStatusHandler extends AbstractExceptionHandler<ResponseStatusException> {

    public ResponseStatusHandler(final ObjectMapper mapper) {
        super(mapper);
    }

    @Override
    public Mono<Void> handle(final ServerWebExchange exchange, final ResponseStatusException exception) {
        prepareExchange(exchange, HttpStatus.NOT_FOUND);
        return Mono.fromCallable(BaseErrorMessage.GENERIC_NOT_FOUND::getMessage)
                .map(message -> buildError(message, HttpStatus.NOT_FOUND))
                .flatMap(problemResponse -> writeResponse(exchange, problemResponse));
    }
}
