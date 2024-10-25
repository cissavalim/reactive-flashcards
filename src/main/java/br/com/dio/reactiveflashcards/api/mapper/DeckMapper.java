package br.com.dio.reactiveflashcards.api.mapper;

import br.com.dio.reactiveflashcards.api.controller.request.DeckRequest;
import br.com.dio.reactiveflashcards.api.controller.response.DeckResponse;
import br.com.dio.reactiveflashcards.domain.document.DeckDocument;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DeckMapper {

    DeckDocument toDocument(final DeckRequest request);

    DeckDocument toDocument(final DeckRequest request, final String id);

    DeckResponse toResponse(final DeckDocument document);
}
