package br.com.cissavalim.reactive_flashcards.domain.service;

import br.com.cissavalim.reactive_flashcards.domain.document.Card;
import br.com.cissavalim.reactive_flashcards.domain.document.Question;
import br.com.cissavalim.reactive_flashcards.domain.document.StudyCard;
import br.com.cissavalim.reactive_flashcards.domain.document.StudyDocument;
import br.com.cissavalim.reactive_flashcards.domain.dto.QuestionDTO;
import br.com.cissavalim.reactive_flashcards.domain.dto.StudyDTO;
import br.com.cissavalim.reactive_flashcards.domain.exception.DeckInStudyException;
import br.com.cissavalim.reactive_flashcards.domain.exception.NotFoundException;
import br.com.cissavalim.reactive_flashcards.domain.mapper.StudyDomainMapper;
import br.com.cissavalim.reactive_flashcards.domain.repository.StudyRepository;
import br.com.cissavalim.reactive_flashcards.domain.service.query.DeckQueryService;
import br.com.cissavalim.reactive_flashcards.domain.service.query.StudyQueryService;
import br.com.cissavalim.reactive_flashcards.domain.service.query.UserQueryService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Random;
import java.util.Set;

import static br.com.cissavalim.reactive_flashcards.domain.exception.BaseErrorMessage.DECK_IN_STUDY;
import static br.com.cissavalim.reactive_flashcards.domain.exception.BaseErrorMessage.STUDY_QUESTION_NOT_FOUND;

@Service
@Slf4j
@AllArgsConstructor
public class StudyService {

    private final UserQueryService userQueryService;
    private final DeckQueryService deckQueryService;
    private final StudyRepository studyRepository;
    private final StudyDomainMapper studyDomainMapper;
    private final StudyQueryService studyQueryService;

    public Mono<StudyDocument> start(final StudyDocument document) {
        return verifyStudy(document)
                .then(Mono.defer(() -> userQueryService.findById(document.userId())))
                .flatMap(user -> deckQueryService.findById(document.studyDeck().deckId()))
                .flatMap(deck -> fillDeckStudyCards(document, deck.cards()))
                .map(study -> study.toBuilder()
                        .question(studyDomainMapper.generateRandomQuestion(study.studyDeck().cards()))
                        .build())
                .doFirst(() -> log.info("generating-a-random-question"))
                .flatMap(studyRepository::save)
                .doOnSuccess(study -> log.info("study-started={}", study.id()));
    }

    private Mono<Void> verifyStudy(final StudyDocument document) {
        final var deckId = document.studyDeck().deckId();
        final var userId = document.userId();

        return studyQueryService.findPendingStudyByUserIdAndDeckId(userId, deckId)
                .flatMap(studyDocument ->
                        Mono.defer(
                                () -> Mono.error(
                                        new DeckInStudyException(DECK_IN_STUDY.params(userId, deckId).getMessage())
                                )
                        )
                )
                .onErrorResume(NotFoundException.class, error -> Mono.empty())
                .then();
    }

    private Mono<StudyDocument> fillDeckStudyCards(final StudyDocument document, final Set<Card> cards) {
        return Flux.fromIterable(cards)
                .doFirst(() -> log.info("copying-cards-to-a-new-study"))
                .map(studyDomainMapper::toStudyCard)
                .collectList()
                .map(studyCards -> document.studyDeck().toBuilder().cards(Set.copyOf(studyCards)).build())
                .map(studyDeck -> document.toBuilder().studyDeck(studyDeck).build());
    }

    public Mono<StudyDocument> answer(final String id, final String answer) {
        return studyQueryService.findById(id)
                .flatMap(studyQueryService::verifyIfFinished)
                .map(study -> studyDomainMapper.answer(study, answer))
                .zipWhen(this::getNextPossibilities)
                .map(tuple -> studyDomainMapper.toDTO(tuple.getT1(), tuple.getT2()))
                .flatMap(this::setNewQuestion)
                .map(studyDomainMapper::toDocument)
                .flatMap(studyRepository::save)
                .doFirst(() -> log.info("answering-question-with-study-id={}", id));
    }

    private Mono<List<String>> getNextPossibilities(final StudyDocument document) {
        return Flux.fromIterable(document.studyDeck().cards())
                .doFirst(() -> log.info("getting-next-possibilities"))
                .map(StudyCard::front)
                .filter(asking -> document.questions().stream()
                        .filter(Question::isCorrect)
                        .map(Question::asked)
                        .noneMatch(question -> question.equals(asking)))
                .collectList()
                .flatMap(askings -> removeLastAsking(askings, document.getLastAnsweredQuestion().asked()));
    }

    private Mono<List<String>> removeLastAsking(final List<String> askings, final String asked) {
        return Mono.just(askings)
                .doFirst(() -> log.info("removing-last-asking"))
                .filter(asking -> asking.size() == 1)
                .switchIfEmpty(
                        Mono.defer(() -> Mono.just(askings.stream()
                                .filter(asking -> !asking.equals(asked))
                                .toList())
                        )
                );
    }

    private Mono<StudyDTO> setNewQuestion(final StudyDTO dto) {
        return Mono.just(dto.hasAnyAnswer())
                .filter(BooleanUtils::isTrue)
                .switchIfEmpty(
                        Mono.defer(
                                () -> Mono.error(new NotFoundException(STUDY_QUESTION_NOT_FOUND.params(dto.id()).getMessage()))
                        )
                )
                .flatMap(hasAnyAnswer -> generateNextQuestion(dto))
                .map(questionDTO -> dto.toBuilder()
                        .question(questionDTO)
                        .build())
                .onErrorResume(NotFoundException.class, error -> Mono.just(dto));
    }

    private Mono<QuestionDTO> generateNextQuestion(final StudyDTO dto) {
        return Mono.just(dto.remainingAskings().get(new Random().nextInt(dto.remainingAskings().size())))
                .map(asking -> dto.studyDeck()
                        .cards()
                        .stream()
                        .filter(card -> card.front().equals(asking))
                        .map(studyDomainMapper::toQuestion)
                        .findFirst()
                        .orElseThrow());
    }
}
