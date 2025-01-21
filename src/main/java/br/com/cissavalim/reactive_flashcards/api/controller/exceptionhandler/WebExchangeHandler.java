package br.com.cissavalim.reactive_flashcards.api.controller.exceptionhandler;

import br.com.cissavalim.reactive_flashcards.api.controller.response.ErrorFieldResponse;
import br.com.cissavalim.reactive_flashcards.api.controller.response.ProblemResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static br.com.cissavalim.reactive_flashcards.domain.exception.BaseErrorMessage.GENERIC_BAD_REQUEST;

@Component
public class WebExchangeHandler extends AbstractExceptionHandler {

    private final MessageSource messageSource;

    public WebExchangeHandler(final ObjectMapper mapper, final MessageSource messageSource) {
        super(mapper);
        this.messageSource = messageSource;
    }

    @Override
    Mono<Void> handleException(final ServerWebExchange exchange, final Throwable ex) {
        return Mono.fromCallable(() -> {
                    prepareExchange(exchange, HttpStatus.BAD_REQUEST);
                    return GENERIC_BAD_REQUEST.getMessage();
                }).map(message -> buildError(HttpStatus.BAD_REQUEST, message))
                .flatMap(response -> buildParamErrorMessage((WebExchangeBindException) ex, response))
                .flatMap(response -> writeResponse(exchange, response));
    }

    @Override
    boolean canHandle(final Throwable ex) {
        return ex instanceof WebExchangeBindException;
    }

    private Mono<ProblemResponse> buildParamErrorMessage(final WebExchangeBindException ex, final ProblemResponse response) {
        return Flux.fromIterable(ex.getAllErrors())
                .map(objectError -> ErrorFieldResponse.builder()
                        .name(objectError instanceof FieldError fieldError ? fieldError.getField() : objectError.getObjectName())
                        .message(messageSource.getMessage(objectError, LocaleContextHolder.getLocale()))
                        .build())
                .collectList()
                .map(problems -> response.toBuilder().fields(problems).build());
    }
}
