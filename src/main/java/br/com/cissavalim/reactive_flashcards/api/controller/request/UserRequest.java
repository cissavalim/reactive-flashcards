package br.com.cissavalim.reactive_flashcards.api.controller.request;

import lombok.Builder;

public record UserRequest(
        String name,
        String email
) {

    @Builder(toBuilder = true)
    public UserRequest {}
}
