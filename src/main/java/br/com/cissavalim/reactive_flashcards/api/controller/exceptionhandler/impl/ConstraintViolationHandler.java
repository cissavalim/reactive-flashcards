package br.com.cissavalim.reactive_flashcards.api.controller.exceptionhandler.impl;

import br.com.cissavalim.reactive_flashcards.api.controller.exceptionhandler.AbstractExceptionHandler;
import br.com.cissavalim.reactive_flashcards.api.controller.response.ErrorFieldResponse;
import br.com.cissavalim.reactive_flashcards.api.controller.response.ProblemResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolationException;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static br.com.cissavalim.reactive_flashcards.domain.exception.BaseErrorMessage.GENERIC_BAD_REQUEST;

@Component
@Order(3)
public class ConstraintViolationHandler extends AbstractExceptionHandler {

    public ConstraintViolationHandler(final ObjectMapper mapper) {
        super(mapper);
    }

    @Override
    public Mono<Void> handleException(final ServerWebExchange exchange, final Throwable ex) {
        return Mono.fromCallable(() -> {
            prepareExchange(exchange, HttpStatus.BAD_REQUEST);
            return GENERIC_BAD_REQUEST.getMessage();
        }).map(message -> buildError(HttpStatus.BAD_REQUEST, message))
                .flatMap(response -> buildParamErrorMessage((ConstraintViolationException) ex, response))
                .flatMap(response -> writeResponse(exchange, response));
    }

    @Override
    public boolean canHandle(final Throwable ex) {
        return ex instanceof ConstraintViolationException;
    }

    private Mono<ProblemResponse> buildParamErrorMessage(final ConstraintViolationException ex, final ProblemResponse response) {
        return Flux.fromIterable(ex.getConstraintViolations())
                .map(violation -> ErrorFieldResponse.builder()
                        .name(((PathImpl) violation.getPropertyPath()).getLeafNode().toString())
                        .message(violation.getMessage()).build())
                .collectList()
                .map(problems -> response.toBuilder().fields(problems).build());
    }

}
