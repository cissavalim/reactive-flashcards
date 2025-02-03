package br.com.cissavalim.reactive_flashcards.domain.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public record StudyDTO(
        String id,
        String userId,
        Boolean complete,
        StudyDeckDTO studyDeck,
        List<QuestionDTO> questions,
        List<String> remainingAskings,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
    public static StudyDTOBuilder builder() {
        return new StudyDTOBuilder();
    }

    public StudyDTOBuilder toBuilder() {
        return new StudyDTOBuilder()
                .id(this.id)
                .userId(this.userId)
                .studyDeck(this.studyDeck)
                .questions(this.questions)
                .createdAt(this.createdAt)
                .updatedAt(this.updatedAt)
                .remainingAskings(this.remainingAskings);
    }

    public Boolean hasAnyAnswer() {
        return CollectionUtils.isNotEmpty(remainingAskings);
    }

    @NoArgsConstructor
    @AllArgsConstructor
    public static class StudyDTOBuilder {
        private String id;
        private String userId;
        private StudyDeckDTO studyDeck;
        private List<QuestionDTO> questions = new ArrayList<>();
        private List<String> remainingAskings = new ArrayList<>();;
        private OffsetDateTime createdAt;
        private OffsetDateTime updatedAt;

        public StudyDTOBuilder id(final String id) {
            this.id = id;
            return this;
        }

        public StudyDTOBuilder userId(final String userId) {
            this.userId = userId;
            return this;
        }

        public StudyDTOBuilder studyDeck(final StudyDeckDTO studyDeck) {
            this.studyDeck = studyDeck;
            return this;
        }

        public StudyDTOBuilder questions(final List<QuestionDTO> questions) {
            this.questions = questions;
            return this;
        }

        public StudyDTOBuilder question(final QuestionDTO question) {
            this.questions.add(question);
            return this;
        }

        public StudyDTOBuilder remainingAskings(final List<String> remainingAskings) {
            this.remainingAskings = remainingAskings;
            return this;
        }

        public StudyDTOBuilder createdAt(final OffsetDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public StudyDTOBuilder updatedAt(final OffsetDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public StudyDTO build() {
            var rightQuestions = questions.stream()
                    .filter(QuestionDTO::isCorrect)
                    .toList();
            var complete = rightQuestions.size() == studyDeck.cards().size();
            return new StudyDTO(id, userId, complete, studyDeck, questions, remainingAskings, createdAt, updatedAt);
        }

    }
}
