package br.com.cissavalim.reactive_flashcards.domain.service;

import br.com.cissavalim.reactive_flashcards.domain.document.UserDocument;
import br.com.cissavalim.reactive_flashcards.domain.exception.EmailAlreadyRegisteredException;
import br.com.cissavalim.reactive_flashcards.domain.exception.NotFoundException;
import br.com.cissavalim.reactive_flashcards.domain.repository.UserRepository;
import br.com.cissavalim.reactive_flashcards.domain.service.query.UserQueryService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Objects;

import static br.com.cissavalim.reactive_flashcards.domain.exception.BaseErrorMessage.EMAIL_ALREADY_REGISTERED;

@Service
@AllArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final UserQueryService userQueryService;

    public Mono<UserDocument> save(final UserDocument userDocument) {
        return userQueryService.findByEmail(userDocument.email())
                .doFirst(() -> log.info("trying-to-save-user={}", userDocument.name()))
                .filter(Objects::isNull)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new EmailAlreadyRegisteredException(
                        EMAIL_ALREADY_REGISTERED.params(userDocument.email()).getMessage()))))
                .onErrorResume(error -> userRepository.save(userDocument));
    }

    private Mono<Void> verifyEmail(final UserDocument userDocument) {
        return userQueryService.findByEmail(userDocument.email())
                .onErrorResume(NotFoundException.class, error -> Mono.empty())
                .filter(stored -> stored.id().equals(userDocument.id()))
                .switchIfEmpty(Mono.defer(() -> Mono.error(new EmailAlreadyRegisteredException(
                        EMAIL_ALREADY_REGISTERED.params(userDocument.email()).getMessage()))))
                .then();
    }

    public Mono<UserDocument> update(final UserDocument userDocument) {
        return verifyEmail(userDocument)
                .then(userQueryService.findById(userDocument.id())
                        .map(user -> userDocument.toBuilder()
                                .createdAt(user.createdAt())
                                .updatedAt(user.updatedAt())
                                .build())
                        .flatMap(userRepository::save)
                        .doFirst(() -> log.info("trying-to-update-user={}", userDocument.name()))
                );
    }

    public Mono<Void> delete(final String id) {
        return userQueryService.findById(id)
                .flatMap(userRepository::delete)
                .doFirst(() -> log.info("trying-to-delete-user={}", id));
    }
}
