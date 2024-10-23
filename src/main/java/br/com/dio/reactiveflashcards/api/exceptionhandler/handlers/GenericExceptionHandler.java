package br.com.dio.reactiveflashcards.api.exceptionhandler.handlers;

import br.com.dio.reactiveflashcards.api.exceptionhandler.AbstractExceptionHandler;
import br.com.dio.reactiveflashcards.domain.exception.BaseErrorMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class GenericExceptionHandler extends AbstractExceptionHandler<Exception> {

    public GenericExceptionHandler(final ObjectMapper mapper) {
        super(mapper);
    }

    @Override
    public Mono<Void> handle(final ServerWebExchange exchange, final Exception exception) {
        prepareExchange(exchange, HttpStatus.INTERNAL_SERVER_ERROR);
        return Mono.fromCallable(BaseErrorMessage.GENERIC_EXCEPTION::getMessage)
                .map(message -> buildError(message, HttpStatus.INTERNAL_SERVER_ERROR))
                .flatMap(problemResponse -> writeResponse(exchange, problemResponse));
    }
}
