package br.com.cissavalim.reactive_flashcards.domain.service;

import br.com.cissavalim.reactive_flashcards.domain.document.Card;
import br.com.cissavalim.reactive_flashcards.domain.document.StudyDocument;
import br.com.cissavalim.reactive_flashcards.domain.mapper.StudyDomainMapper;
import br.com.cissavalim.reactive_flashcards.domain.repository.StudyRepository;
import br.com.cissavalim.reactive_flashcards.domain.service.query.DeckQueryService;
import br.com.cissavalim.reactive_flashcards.domain.service.query.UserQueryService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Set;

@Service
@Slf4j
@AllArgsConstructor
public class StudyService {

    private final UserQueryService userQueryService;
    private final DeckQueryService deckQueryService;
    private final StudyRepository studyRepository;
    private final StudyDomainMapper studyDomainMapper;

    public Mono<StudyDocument> start(final StudyDocument document) {
        return userQueryService.findById(document.userId())
                .flatMap(user -> deckQueryService.findById(document.studyDeck().deckId()))
                .flatMap(deck -> getCards(document, deck.cards()))
                .map(study -> study.toBuilder()
                        .question(studyDomainMapper.generateRandomQuestion(study.studyDeck().cards())).build())
                .doFirst(() -> log.info("generating-a-random-question"))
                .flatMap(studyRepository::save)
                .doOnSuccess(study -> log.info("study-started={}", study.id()));
    }

    public Mono<StudyDocument> getCards(final StudyDocument document, final Set<Card> cards) {
        return Flux.fromIterable(cards)
                .doFirst(() -> log.info("copying-cards-to-a-new-study"))
                .map(studyDomainMapper::toStudyCard)
                .collectList()
                .map(studyCards -> document.studyDeck().toBuilder().cards(Set.copyOf(studyCards)).build())
                .map(studyDeck -> document.toBuilder().studyDeck(studyDeck).build());
    }
}
