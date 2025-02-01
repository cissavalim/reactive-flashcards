package br.com.cissavalim.reactive_flashcards.domain.service.query;

import br.com.cissavalim.reactive_flashcards.domain.document.Question;
import br.com.cissavalim.reactive_flashcards.domain.document.StudyDocument;
import br.com.cissavalim.reactive_flashcards.domain.exception.NotFoundException;
import br.com.cissavalim.reactive_flashcards.domain.repository.StudyRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

import static br.com.cissavalim.reactive_flashcards.domain.exception.BaseErrorMessage.*;

@Service
@Slf4j
@AllArgsConstructor
public class StudyQueryService {

    private final StudyRepository studyRepository;

    public Mono<StudyDocument> findPendingStudyByUserIdAndDeckId(final String userId, final String deckId) {
        return studyRepository.findByUserIdAndCompleteFalseAndStudyDeck_DeckId(userId, deckId)
                .doFirst(() -> log.info("finding-pending-study"))
                .filter(Objects::nonNull)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new NotFoundException(STUDY_DECK_NOT_FOUND.params(userId, deckId).getMessage()))));
    }

    public Mono<StudyDocument> findById(final String id) {
        return studyRepository.findById(id)
                .doFirst(() -> log.info("finding-study-by-id"))
                .filter(Objects::nonNull)
                .switchIfEmpty(
                        Mono.defer(
                                () -> Mono.error(new NotFoundException(STUDY_NOT_FOUND.params(id).getMessage()))
                        )
                );
    }

    public Mono<Question> getLastPendingQuestion(final String id) {
        return findById(id)
                .filter(study -> BooleanUtils.isFalse(study.complete()))
                .switchIfEmpty(
                        Mono.defer(
                                () -> Mono.error(new NotFoundException(STUDY_QUESTION_NOT_FOUND
                                        .params(id)
                                        .getMessage()))
                        )
                )
                .flatMapMany(study -> Flux.fromIterable(study.questions()))
                .filter(Question::isAnswered)
                .doFirst(() -> log.info("getting-last-pending-question"))
                .single();

    }
}
