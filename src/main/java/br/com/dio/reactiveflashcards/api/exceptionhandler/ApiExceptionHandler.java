package br.com.dio.reactiveflashcards.api.exceptionhandler;

import br.com.dio.reactiveflashcards.api.controller.response.ProblemResponse;
import br.com.dio.reactiveflashcards.domain.exception.BaseErrorMessage;
import br.com.dio.reactiveflashcards.domain.exception.ReactiveFlashcardsException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.MethodNotAllowedException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

@Component
@Order(-2)
@Slf4j
@AllArgsConstructor
public class ApiExceptionHandler implements WebExceptionHandler {

    private final ObjectMapper mapper;

    @Override
    public Mono<Void> handle(final ServerWebExchange exchange, final Throwable ex) {
        log.error("exception={}", ex.getMessage(), ex);

        return Mono.error(ex)
                .onErrorResume(MethodNotAllowedException.class, e -> handleMethodNotAllowedException(exchange))
                .onErrorResume(ReactiveFlashcardsException.class, e -> handleReactiveFlashcardsException(exchange))
                .onErrorResume(Exception.class, e -> handleException(exchange))
                .onErrorResume(JsonProcessingException.class, e -> handleJsonProcessingException(exchange))
                .then();
    }

    private Mono<Void> handleMethodNotAllowedException(final ServerWebExchange exchange) {
        return handleError(BaseErrorMessage.GENERIC_METHOD_NOT_ALLOWED, HttpStatus.METHOD_NOT_ALLOWED, exchange);
    }

    private Mono<Void> handleReactiveFlashcardsException(final ServerWebExchange exchange) {
        return handleError(BaseErrorMessage.GENERIC_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR, exchange);
    }

    private Mono<Void> handleException(final ServerWebExchange exchange) {
        return handleError(BaseErrorMessage.GENERIC_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR, exchange);
    }

    private Mono<Void> handleJsonProcessingException(final ServerWebExchange exchange) {
        return handleError(BaseErrorMessage.GENERIC_METHOD_NOT_ALLOWED, HttpStatus.METHOD_NOT_ALLOWED, exchange);
    }

    private Mono<Void> handleError(final BaseErrorMessage baseErrorMessage, final HttpStatus status, final ServerWebExchange exchange) {
        prepareExchange(exchange, status);
        return Mono.fromCallable(baseErrorMessage::getMessage)
                .map(message -> buildError(status, message))
                .flatMap(problemResponse -> writeResponse(exchange, problemResponse));
    }

    private void prepareExchange(final ServerWebExchange exchange, final HttpStatus status) {
        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
    }

    private ProblemResponse buildError(final HttpStatus status, final String errorDescription) {
        return ProblemResponse.builder()
                .status(status.value())
                .errorDescription(errorDescription)
                .build();
    }

    private Mono<Void> writeResponse(final ServerWebExchange exchange, final ProblemResponse problemResponse) {
        return exchange.getResponse()
                .writeWith(Mono.fromCallable(
                        () -> new DefaultDataBufferFactory().wrap(mapper.writeValueAsBytes(problemResponse))));
    }
}
