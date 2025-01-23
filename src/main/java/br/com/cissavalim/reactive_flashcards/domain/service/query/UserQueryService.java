package br.com.cissavalim.reactive_flashcards.domain.service.query;

import br.com.cissavalim.reactive_flashcards.domain.document.UserDocument;
import br.com.cissavalim.reactive_flashcards.domain.exception.NotFoundException;
import br.com.cissavalim.reactive_flashcards.domain.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Objects;

import static br.com.cissavalim.reactive_flashcards.domain.exception.BaseErrorMessage.USER_NOT_FOUND;

@Service
@Slf4j
@AllArgsConstructor
public class UserQueryService {

    private final UserRepository userRepository;

    public Mono<UserDocument> findById(final String id) {
        return userRepository.findById(id)
                .doFirst(() -> log.info("find-user-by-id={}", id))
                .filter(Objects::nonNull)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new NotFoundException(USER_NOT_FOUND.params("id", id).getMessage()))));
    }

    public Mono<UserDocument> findByEmail(final String email) {
        return userRepository.findByEmail(email)
                .doFirst(() -> log.info("find-user-by-email={}", email))
                .filter(Objects::nonNull)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new NotFoundException(USER_NOT_FOUND.params("email", email).getMessage()))));
    }
}
