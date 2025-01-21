package br.com.cissavalim.reactive_flashcards.api.controller.exceptionhandler;

import br.com.cissavalim.reactive_flashcards.api.controller.exceptionhandler.handlerchain.HandlerChain;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

@Component
@Order(-2)
@Slf4j
@AllArgsConstructor
public class ApiExceptionHandler implements WebExceptionHandler {

    private final HandlerChain handlerChain;

    @Override
    public Mono<Void> handle(final ServerWebExchange exchange, final Throwable ex) {
        log.error("handling-exception={}", ex.getMessage(), ex);
        return handlerChain.handle(exchange, ex);
    }
}