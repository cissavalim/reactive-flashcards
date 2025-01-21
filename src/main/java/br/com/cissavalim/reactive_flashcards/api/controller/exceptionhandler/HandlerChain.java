package br.com.cissavalim.reactive_flashcards.api.controller.exceptionhandler;

import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class HandlerChain {

    private final List<AbstractExceptionHandler> handlers;

    public HandlerChain(List<AbstractExceptionHandler> handlers) {
        this.handlers = handlers;
    }

    public Mono<Void> handle(final ServerWebExchange exchange, final Throwable ex) {
        for (AbstractExceptionHandler handler : handlers) {
            if (handler.canHandle(ex)) {
                return handler.handleException(exchange, ex);
            }
        }
        return Mono.error(ex);
    }
}