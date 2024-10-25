package br.com.dio.reactiveflashcards.domain.service;

import br.com.dio.reactiveflashcards.domain.document.UserDocument;
import br.com.dio.reactiveflashcards.domain.repository.UserRepository;
import br.com.dio.reactiveflashcards.domain.service.query.UserQueryService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.OffsetDateTime;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserQueryService userQueryService;

    public Mono<UserDocument> save(UserDocument userDocument) {
        return userRepository.save(userDocument)
                .doAfterTerminate(() -> log.info("saved-user={}", userDocument.id()));
    }

    public Mono<UserDocument> update(UserDocument userDocument) {
        return userQueryService.findById(userDocument.id())
                .map(user -> userDocument.toBuilder()
                        .createdAt(user.createdAt())
                        .updatedAt(user.updatedAt())
                        .build())
                .flatMap(userRepository::save)
                .doFirst(() -> log.info("updating-user={}", userDocument.id()));
    }

    public Mono<Void> delete(final String id){
        return userQueryService.findById(id)
                .flatMap(userRepository::delete)
                .doAfterTerminate(() -> log.info("deleted-user={}", id));
    }
}
