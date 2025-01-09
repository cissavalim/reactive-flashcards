package br.com.cissavalim.reactive_flashcards.api.mapper;

import br.com.cissavalim.reactive_flashcards.api.controller.request.UserRequest;
import br.com.cissavalim.reactive_flashcards.api.controller.response.UserResponse;
import br.com.cissavalim.reactive_flashcards.domain.document.UserDocument;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    UserDocument toDocument(final UserRequest request);

    UserResponse toResponse(final UserDocument document);
}
