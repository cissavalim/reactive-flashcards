package br.com.cissavalim.reactive_flashcards.domain.service;

import br.com.cissavalim.reactive_flashcards.domain.document.DeckDocument;
import br.com.cissavalim.reactive_flashcards.domain.repository.DeckRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@AllArgsConstructor
public class DeckService {

    public final DeckRepository deckRepository;

    public Mono<DeckDocument> save(final DeckDocument document) {
        return deckRepository.save(document)
                .doFirst(() -> log.info("trying-to-save-deck={}", document));
    }
}
