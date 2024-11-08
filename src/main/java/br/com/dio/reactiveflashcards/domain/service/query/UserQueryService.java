package br.com.dio.reactiveflashcards.domain.service.query;

import br.com.dio.reactiveflashcards.domain.document.UserDocument;
import br.com.dio.reactiveflashcards.domain.exception.NotFoundException;
import br.com.dio.reactiveflashcards.domain.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Objects;

import static br.com.dio.reactiveflashcards.domain.exception.BaseErrorMessage.USER_NOT_FOUND;

@Service
@Slf4j
@AllArgsConstructor
public class UserQueryService {

    private final UserRepository userRepository;

    public Mono<UserDocument> findById(final String id) {
        return userRepository.findById(id)
                .doFirst(() -> log.info("finding-user-id={}", id))
                .filter(Objects::nonNull)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new NotFoundException(USER_NOT_FOUND.params(id).getMessage()))));
    }
}
