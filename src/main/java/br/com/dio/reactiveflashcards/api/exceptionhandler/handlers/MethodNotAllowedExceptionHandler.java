package br.com.dio.reactiveflashcards.api.exceptionhandler.handlers;

import br.com.dio.reactiveflashcards.api.exceptionhandler.AbstractExceptionHandler;
import br.com.dio.reactiveflashcards.domain.exception.BaseErrorMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.MethodNotAllowedException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class MethodNotAllowedExceptionHandler extends AbstractExceptionHandler<MethodNotAllowedException> {

    public MethodNotAllowedExceptionHandler(final ObjectMapper mapper){
        super(mapper);
    }

    @Override
    public Mono<Void> handle(final ServerWebExchange exchange, final MethodNotAllowedException exception) {
        prepareExchange(exchange, HttpStatus.METHOD_NOT_ALLOWED);
        return Mono.fromCallable(() -> BaseErrorMessage.GENERIC_METHOD_NOT_ALLOWED.params(exchange.getRequest().getMethod().name()).getMessage())
                .map(message -> buildError(message, HttpStatus.METHOD_NOT_ALLOWED))
                .flatMap(problemResponse -> writeResponse(exchange, problemResponse));
    }
}
