package br.com.dio.reactiveflashcards.api.exceptionhandler.handlers;

import br.com.dio.reactiveflashcards.api.exceptionhandler.AbstractExceptionHandler;
import br.com.dio.reactiveflashcards.domain.exception.BaseErrorMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class JsonProcessingExceptionHandler extends AbstractExceptionHandler<JsonProcessingException> {

    public JsonProcessingExceptionHandler(final ObjectMapper mapper) {
        super(mapper);
    }

    @Override
    public Mono<Void> handle(final ServerWebExchange exchange, final JsonProcessingException exception) {
        prepareExchange(exchange, HttpStatus.METHOD_NOT_ALLOWED);
        return Mono.fromCallable(BaseErrorMessage.GENERIC_EXCEPTION::getMessage)
                .map(message -> buildError(message, HttpStatus.METHOD_NOT_ALLOWED))
                .flatMap(problemResponse -> writeResponse(exchange, problemResponse));
    }
}
