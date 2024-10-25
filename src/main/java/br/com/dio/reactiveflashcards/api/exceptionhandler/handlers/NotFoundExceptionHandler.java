package br.com.dio.reactiveflashcards.api.exceptionhandler.handlers;

import br.com.dio.reactiveflashcards.api.exceptionhandler.AbstractExceptionHandler;
import br.com.dio.reactiveflashcards.domain.exception.BaseErrorMessage;
import br.com.dio.reactiveflashcards.domain.exception.NotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class NotFoundExceptionHandler extends AbstractExceptionHandler<NotFoundException> {

    public NotFoundExceptionHandler(final ObjectMapper mapper) {
        super(mapper);
    }

    @Override
    public Mono<Void> handle(final ServerWebExchange exchange, final NotFoundException exception) {
        prepareExchange(exchange, HttpStatus.NOT_FOUND);
        return Mono.fromCallable(exception::getMessage)
                .map(message -> buildError(message, HttpStatus.NOT_FOUND))
                .flatMap(problemResponse -> writeResponse(exchange, problemResponse));
    }
}
