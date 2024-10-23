package br.com.dio.reactiveflashcards.api.exceptionhandler.handlers;

import br.com.dio.reactiveflashcards.api.controller.response.ErrorFieldResponse;
import br.com.dio.reactiveflashcards.api.controller.response.ProblemResponse;
import br.com.dio.reactiveflashcards.api.exceptionhandler.AbstractExceptionHandler;
import br.com.dio.reactiveflashcards.domain.exception.BaseErrorMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class WebExchangeBindExceptionHandler extends AbstractExceptionHandler<WebExchangeBindException> {

    private final MessageSource messageSource;

    public WebExchangeBindExceptionHandler(final ObjectMapper mapper, final MessageSource messageSource) {
        super(mapper);
        this.messageSource = messageSource;
    }

    @Override
    public Mono<Void> handle(final ServerWebExchange exchange, final WebExchangeBindException exception) {
        prepareExchange(exchange, HttpStatus.INTERNAL_SERVER_ERROR);
        return Mono.fromCallable(BaseErrorMessage.GENERIC_BAD_REQUEST::getMessage)
                .map(message -> buildError(message, HttpStatus.METHOD_NOT_ALLOWED))
                .flatMap(problemResponse -> build(problemResponse, exception))
                .flatMap(problemResponse -> writeResponse(exchange, problemResponse));
    }

    private Mono<ProblemResponse> build(final ProblemResponse response, final WebExchangeBindException ex) {
        return Flux.fromIterable(ex.getAllErrors())
                .map(objectError -> ErrorFieldResponse.builder()
                        .name(objectError instanceof FieldError fieldError ? fieldError.getField() : objectError.getObjectName())
                        .message(messageSource.getMessage(objectError, LocaleContextHolder.getLocale()))
                        .build())
                .collectList()
                .map(errorFieldResponses -> response.toBuilder()
                        .fields(errorFieldResponses)
                        .build());
    }
}
