package br.com.cissavalim.reactive_flashcards.domain.service;

import br.com.cissavalim.reactive_flashcards.domain.document.UserDocument;
import br.com.cissavalim.reactive_flashcards.domain.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    public Mono<UserDocument> save(final UserDocument userDocument) {
        return userRepository.save(userDocument)
                .doFirst(() -> log.info("trying-to-save-user={}", userDocument.name()));
    }
}
