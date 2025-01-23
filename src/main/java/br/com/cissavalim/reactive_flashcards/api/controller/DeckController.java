package br.com.cissavalim.reactive_flashcards.api.controller;

import br.com.cissavalim.reactive_flashcards.api.controller.request.DeckRequest;
import br.com.cissavalim.reactive_flashcards.api.controller.response.DeckResponse;
import br.com.cissavalim.reactive_flashcards.api.mapper.DeckMapper;
import br.com.cissavalim.reactive_flashcards.domain.service.DeckService;
import br.com.cissavalim.reactive_flashcards.domain.service.query.DeckQueryService;
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
@RequestMapping("decks")
@Slf4j
@AllArgsConstructor
public class DeckController {

    public final DeckService deckService;
    public final DeckQueryService deckQueryService;
    public final DeckMapper deckMapper;

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(CREATED)
    public Mono<DeckResponse> save(@Valid @RequestBody final DeckRequest deckRequest) {
        return deckService.save(deckMapper.toDocument(deckRequest))
                .doFirst(() -> log.info("saving-deck-with-name={}", deckRequest.name()))
                .map(deckMapper::toResponse);
    }

    @GetMapping(produces = APPLICATION_JSON_VALUE, value = "{id}")
    public Mono<DeckResponse> findById(@PathVariable final String id) {
        return deckQueryService.findById(id)
                .doFirst(() -> log.info("find-deck-by-id={}", id))
                .map(deckMapper::toResponse);
    }

}
