package br.com.cissavalim.reactive_flashcards.api.controller.response;

import lombok.Builder;

public record UserResponse(
        String id,
        String name,
        String email
) {

    @Builder(toBuilder = true)
    public UserResponse {
    }
}
