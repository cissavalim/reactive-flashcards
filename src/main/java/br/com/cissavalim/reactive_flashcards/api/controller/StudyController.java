package br.com.cissavalim.reactive_flashcards.api.controller;

import br.com.cissavalim.reactive_flashcards.api.controller.request.StudyRequest;
import br.com.cissavalim.reactive_flashcards.api.controller.response.QuestionResponse;
import br.com.cissavalim.reactive_flashcards.api.mapper.StudyMapper;
import br.com.cissavalim.reactive_flashcards.core.validation.MongoId;
import br.com.cissavalim.reactive_flashcards.domain.service.StudyService;
import br.com.cissavalim.reactive_flashcards.domain.service.query.StudyQueryService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@Validated
@RequestMapping("studies")
@Slf4j
@AllArgsConstructor
public class StudyController {

    private final StudyService studyService;
    private final StudyQueryService studyQueryService;
    private final StudyMapper studyMapper;

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(CREATED)
    public Mono<QuestionResponse> start(@RequestBody @Valid final StudyRequest request) {
        return studyService.start(studyMapper.toDocument(request))
                .doFirst(() -> log.info("starting-study-with-deck-id={}", request.deckId()))
                .map(document -> studyMapper.toResponse(document.getLastPendingQuestion(), document.id()));
    }

    @GetMapping(produces = APPLICATION_JSON_VALUE, value = "{id}/current-question")
    public Mono<QuestionResponse> getCurrentQuestion(@Valid @PathVariable @MongoId(message = "{studyController.id}") final String id) {
        return studyQueryService.getLastPendingQuestion(id)
                .doFirst(() -> log.info("getting-current-question"))
                .map(question -> studyMapper.toResponse(question, id));
    }
}
