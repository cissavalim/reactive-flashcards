package br.com.cissavalim.reactive_flashcards.api.controller.exceptionhandler;

import br.com.cissavalim.reactive_flashcards.api.controller.response.ProblemResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@AllArgsConstructor
public abstract class AbstractExceptionHandler {

    private final ObjectMapper mapper;

    abstract Mono<Void> handleException(final ServerWebExchange exchange, final Throwable ex);

    abstract boolean canHandle(final Throwable ex);

    public Mono<Void> writeResponse(final ServerWebExchange exchange, final ProblemResponse response) {
        return exchange.getResponse()
                .writeWith(Mono.fromCallable(() -> new DefaultDataBufferFactory().wrap(mapper.writeValueAsBytes(response))));
    }

    public ProblemResponse buildError(final HttpStatus status, final String errorDescription) {
        return ProblemResponse.builder()
                .status(status.value())
                .description(errorDescription)
                .build();
    }

    public void prepareExchange(final ServerWebExchange exchange, final HttpStatus status) {
        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders().setContentType(APPLICATION_JSON);
    }
}
