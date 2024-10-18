package br.com.dio.reactiveflashcards.api.mapper;

import br.com.dio.reactiveflashcards.api.controller.request.UserRequest;
import br.com.dio.reactiveflashcards.api.controller.response.UserResponse;
import br.com.dio.reactiveflashcards.domain.document.UserDocument;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDocument toDocument(final UserRequest userRequest);

    UserResponse toResponse(final UserDocument userDocument);
}
