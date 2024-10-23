package br.com.dio.reactiveflashcards.api.exceptionhandler.handlers;

import br.com.dio.reactiveflashcards.api.controller.response.ErrorFieldResponse;
import br.com.dio.reactiveflashcards.api.controller.response.ProblemResponse;
import br.com.dio.reactiveflashcards.api.exceptionhandler.AbstractExceptionHandler;
import br.com.dio.reactiveflashcards.domain.exception.BaseErrorMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolationException;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class ConstraintViolationExceptionHandler extends AbstractExceptionHandler<ConstraintViolationException> {

    public ConstraintViolationExceptionHandler(final ObjectMapper mapper) {
        super(mapper);
    }

    @Override
    public Mono<Void> handle(final ServerWebExchange exchange, final ConstraintViolationException exception) {
        prepareExchange(exchange, HttpStatus.INTERNAL_SERVER_ERROR);
        return Mono.fromCallable(BaseErrorMessage.GENERIC_BAD_REQUEST::getMessage)
                .map(message -> buildError(message, HttpStatus.INTERNAL_SERVER_ERROR))
                .flatMap(problemResponse -> buildRequestErrorMessage(problemResponse, exception))
                .flatMap(problemResponse -> writeResponse(exchange, problemResponse));
    }

    private Mono<ProblemResponse> buildRequestErrorMessage(final ProblemResponse response, final ConstraintViolationException exception) {
        return Flux.fromIterable(exception.getConstraintViolations())
                .map(constraintViolation -> ErrorFieldResponse.builder()
                        .name(((PathImpl) constraintViolation.getPropertyPath()).getLeafNode().toString())
                        .message(constraintViolation.getMessage())
                        .build())
                .collectList()
                .map(errorFieldResponses -> response.toBuilder()
                        .fields(errorFieldResponses)
                        .build());
    }
}
