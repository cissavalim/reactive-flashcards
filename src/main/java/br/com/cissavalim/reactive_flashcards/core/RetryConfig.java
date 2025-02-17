package br.com.cissavalim.reactive_flashcards.core;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

import java.time.Duration;

@ConfigurationProperties(prefix = "retry-config")
public record RetryConfig(
        Long maxRetries,
        Long minDuration
) {

    @ConstructorBinding
    public RetryConfig {}

    public Duration minDurationInSeconds() {
        return Duration.ofSeconds(minDuration);
    }
}
