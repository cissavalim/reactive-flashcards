package br.com.dio.reactiveflashcards.api.controller;

import br.com.dio.reactiveflashcards.api.controller.request.DeckRequest;
import br.com.dio.reactiveflashcards.api.controller.response.DeckResponse;
import br.com.dio.reactiveflashcards.api.mapper.DeckMapper;
import br.com.dio.reactiveflashcards.domain.service.DeckService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Validated
@RestController
@RequestMapping("decks")
@Slf4j
@AllArgsConstructor
public class DeckController {

    private final DeckService deckService;
    private final DeckMapper deckMapper;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<DeckResponse> save(@Valid @RequestBody final DeckRequest deckRequest) {
        return deckService.save(deckMapper.toDocument(deckRequest))
                .doFirst(() -> log.info("saving-deck={}", deckRequest.name()))
                .map(deckMapper::toResponse);
    }
}
