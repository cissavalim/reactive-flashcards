package br.com.dio.reactiveflashcards.api.exceptionhandler;

import br.com.dio.reactiveflashcards.api.controller.response.ProblemResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.OffsetDateTime;

@RequiredArgsConstructor
public abstract class AbstractExceptionHandler<T extends Throwable> {

    private final ObjectMapper mapper;

    protected abstract Mono<Void> handle(final ServerWebExchange exchange, final T exception);

    protected void prepareExchange(final ServerWebExchange exchange, HttpStatus status) {
        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
    }

    protected ProblemResponse buildError(final String errorDescription, HttpStatus status) {
        return ProblemResponse.builder()
                .status(status.value())
                .errorDescription(errorDescription)
                .timestamp(OffsetDateTime.now())
                .build();
    }

    protected Mono<Void> writeResponse(final ServerWebExchange exchange, final ProblemResponse problemResponse) {
        return exchange.getResponse()
                .writeWith(Mono.fromCallable(
                        () -> new DefaultDataBufferFactory().wrap(mapper.writeValueAsBytes(problemResponse))));
    }
}
