package br.com.cissavalim.reactive_flashcards.domain.helper;

import br.com.cissavalim.reactive_flashcards.core.RetryConfig;
import br.com.cissavalim.reactive_flashcards.domain.exception.RetryException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.util.retry.Retry;

import java.util.function.Predicate;

import static br.com.cissavalim.reactive_flashcards.domain.exception.BaseErrorMessage.GENERIC_MAX_RETRIES;

@Component
@Slf4j
@RequiredArgsConstructor
public class RetryHelper {

    private final RetryConfig retryConfig;

    public Retry processRetry(final String retryIdentifier, final Predicate<? super Throwable> errorFilter) {
        return Retry
                .backoff(retryConfig.maxRetries(), retryConfig.minDurationInSeconds())
                .filter(errorFilter)
                .doBeforeRetry(retrySignal -> log.warn("Retrying {} after {} attempts", retryIdentifier, retrySignal.totalRetriesInARow()))
                .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) ->
                        new RetryException(GENERIC_MAX_RETRIES.getMessage(), retrySignal.failure()));
    }
}
