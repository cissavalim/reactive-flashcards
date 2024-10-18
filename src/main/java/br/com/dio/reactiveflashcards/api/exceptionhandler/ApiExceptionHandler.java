package br.com.dio.reactiveflashcards.api.exceptionhandler;

import br.com.dio.reactiveflashcards.api.controller.response.ProblemResponse;
import br.com.dio.reactiveflashcards.domain.exception.BaseErrorMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
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
        return Mono.error(ex)
                .onErrorResume(Exception.class, e -> handleException(exchange, e))
                .then();
    }

    private Mono<Void> handleException(final ServerWebExchange exchange, final Exception ex) {
        prepareExchange(exchange, HttpStatus.INTERNAL_SERVER_ERROR);

        return Mono.fromCallable(BaseErrorMessage.GENERIC_EXCEPTION::getMessage)
                .map(message -> buildError(HttpStatus.INTERNAL_SERVER_ERROR, message))
                .flatMap(problemResponse -> writeResponse(exchange, problemResponse));
    }

    private void prepareExchange(final ServerWebExchange exchange, final HttpStatus status) {
        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
    }

    private ProblemResponse buildError(final HttpStatus status, final String errorDesciption) {
        return ProblemResponse.builder()
                .status(status.value())
                .errorDescription(errorDesciption)
                .build();
    }

    private Mono<Void> writeResponse(final ServerWebExchange exchange, final ProblemResponse problemResponse) {
        return exchange.getResponse()
                .writeWith(Mono.fromCallable(
                        () -> new DefaultDataBufferFactory().wrap(mapper.writeValueAsBytes(problemResponse))));
    }
}
