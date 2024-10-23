package br.com.dio.reactiveflashcards.api.exceptionhandler.handlers;

import br.com.dio.reactiveflashcards.api.controller.response.ProblemResponse;
import br.com.dio.reactiveflashcards.api.exceptionhandler.AbstractExceptionHandler;
import br.com.dio.reactiveflashcards.domain.exception.BaseErrorMessage;
import br.com.dio.reactiveflashcards.domain.exception.ReactiveFlashcardsException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class ReactiveFlashcardsExceptionHandler extends AbstractExceptionHandler<ReactiveFlashcardsException> {

    public ReactiveFlashcardsExceptionHandler(final ObjectMapper mapper) {
        super(mapper);
    }

    @Override
    public Mono<Void> handle(final ServerWebExchange exchange, final ReactiveFlashcardsException exception) {
        prepareExchange(exchange, HttpStatus.INTERNAL_SERVER_ERROR);
        return Mono.fromCallable(BaseErrorMessage.GENERIC_EXCEPTION::getMessage)
                .map(message -> buildError(message, HttpStatus.INTERNAL_SERVER_ERROR))
                .flatMap(problemResponse -> writeResponse(exchange, problemResponse));
    }
}
