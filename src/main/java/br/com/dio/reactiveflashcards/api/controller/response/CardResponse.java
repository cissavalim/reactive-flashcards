package br.com.dio.reactiveflashcards.api.controller.response;

import lombok.Builder;

public record CardResponse(String id, String front, String back) {

    @Builder(toBuilder = true)
    public CardResponse {
    }
}
