package br.com.dio.reactiveflashcards.api.exceptionhandler;

import br.com.dio.reactiveflashcards.api.exceptionhandler.handlers.*;
import br.com.dio.reactiveflashcards.domain.exception.NotFoundException;
import br.com.dio.reactiveflashcards.domain.exception.ReactiveFlashcardsException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolationException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.context.MessageSource;
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

    private final ObjectMapper mapper;
    private final MessageSource messageSource;

    @Override
    public Mono<Void> handle(final ServerWebExchange exchange, final Throwable exception) {
        log.error("handling-exception: {}", exception.getMessage(), exception);

        return Mono.error(exception)
                .onErrorResume(MethodNotAllowedException.class, e -> new MethodNotAllowedExceptionHandler(mapper).handle(exchange, e))
                .onErrorResume(NotFoundException.class, e -> new NotFoundExceptionHandler(mapper).handle(exchange, e))
                .onErrorResume(ConstraintViolationException.class, e -> new ConstraintViolationExceptionHandler(mapper).handle(exchange, e))
                .onErrorResume(WebExchangeBindException.class, e -> new WebExchangeBindExceptionHandler(mapper, messageSource).handle(exchange, e))
                .onErrorResume(ResponseStatusException.class, e -> new ResponseStatusHandler(mapper).handle(exchange, e))
                .onErrorResume(ReactiveFlashcardsException.class, e -> new ReactiveFlashcardsExceptionHandler(mapper).handle(exchange, e))
                .onErrorResume(Exception.class, e -> new GenericExceptionHandler(mapper).handle(exchange, e))
                .onErrorResume(JsonProcessingException.class, e -> new JsonProcessingExceptionHandler(mapper).handle(exchange, e))
                .then();
    }
}
