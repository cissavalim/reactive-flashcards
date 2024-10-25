package br.com.dio.reactiveflashcards.api.exceptionhandler;

import br.com.dio.reactiveflashcards.api.exceptionhandler.handlers.*;
import br.com.dio.reactiveflashcards.domain.exception.NotFoundException;
import br.com.dio.reactiveflashcards.domain.exception.ReactiveFlashcardsException;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.ConstraintViolationException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.MethodNotAllowedException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

@Component
@Order(-2)
@Slf4j
@AllArgsConstructor
public class ApiExceptionHandler implements WebExceptionHandler {

    private final MethodNotAllowedExceptionHandler methodNotAllowedExceptionHandler;
    private final NotFoundExceptionHandler notFoundExceptionHandler;
    private final ConstraintViolationExceptionHandler constraintViolationExceptionHandler;
    private final WebExchangeBindExceptionHandler webExchangeBindExceptionHandler;
    private final ResponseStatusHandler responseStatusHandler;
    private final ReactiveFlashcardsExceptionHandler reactiveFlashcardsExceptionHandler;
    private final GenericExceptionHandler genericExceptionHandler;
    private final JsonProcessingExceptionHandler jsonProcessingExceptionHandler;

    @Override
    public Mono<Void> handle(final ServerWebExchange exchange, final Throwable exception) {
        log.error("handling-exception: {}", exception.getMessage(), exception);

        return Mono.error(exception)
                .onErrorResume(MethodNotAllowedException.class, e -> methodNotAllowedExceptionHandler.handle(exchange, e))
                .onErrorResume(NotFoundException.class, e -> notFoundExceptionHandler.handle(exchange, e))
                .onErrorResume(ConstraintViolationException.class, e -> constraintViolationExceptionHandler.handle(exchange, e))
                .onErrorResume(WebExchangeBindException.class, e -> webExchangeBindExceptionHandler.handle(exchange, e))
                .onErrorResume(ResponseStatusException.class, e -> responseStatusHandler.handle(exchange, e))
                .onErrorResume(ReactiveFlashcardsException.class, e -> reactiveFlashcardsExceptionHandler.handle(exchange, e))
                .onErrorResume(Exception.class, e -> genericExceptionHandler.handle(exchange, e))
                .onErrorResume(JsonProcessingException.class, e -> jsonProcessingExceptionHandler.handle(exchange, e))
                .then();
    }
}
