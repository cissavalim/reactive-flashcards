package br.com.cissavalim.reactive_flashcards.domain.document;

import lombok.Builder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.OffsetDateTime;
import java.util.Set;

@Document(collection = "decks")
public record DeckDocument(
        @Id
        String id,
        String name,
        String description,
        Set<Card> cards,
        @CreatedDate
        OffsetDateTime createdAt,
        @LastModifiedDate
        OffsetDateTime updatedAt
) {

    @Builder(toBuilder = true)
    public DeckDocument {
    }
}
