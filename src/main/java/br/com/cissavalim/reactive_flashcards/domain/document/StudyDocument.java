package br.com.cissavalim.reactive_flashcards.domain.document;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Document(collection = "studies")
public record StudyDocument(
        @Id
        String id,
        String userId,
        Boolean complete,
        StudyDeck studyDeck,
        List<Question> questions,
        @CreatedDate
        OffsetDateTime createdAt,
        @LastModifiedDate
        OffsetDateTime updatedAt
) {
    public static StudyDocumentBuilder builder() {
        return new StudyDocumentBuilder();
    }

    public StudyDocumentBuilder toBuilder() {
        return new StudyDocumentBuilder(id, userId, studyDeck, questions, createdAt, updatedAt);
    }

    public Question getLastPendingQuestion() {
        return questions.stream()
                .filter(question -> Objects.isNull(question.answeredAt()))
                .findFirst()
                .orElseThrow();
    }

    public Question getLastAnsweredQuestion() {
        return questions.stream()
                .filter(question -> Objects.nonNull(question.answeredAt()))
                .max(Comparator.comparing(Question::answeredAt))
                .orElseThrow();
    }

    @NoArgsConstructor
    @AllArgsConstructor
    public static class StudyDocumentBuilder {
        private String id;
        private String userId;
        private StudyDeck studyDeck;
        private List<Question> questions = new ArrayList<>();
        private OffsetDateTime createdAt;
        private OffsetDateTime updatedAt;

        public StudyDocumentBuilder id(final String id) {
            this.id = id;
            return this;
        }

        public StudyDocumentBuilder userId(final String userId) {
            this.userId = userId;
            return this;
        }

        public StudyDocumentBuilder studyDeck(final StudyDeck studyDeck) {
            this.studyDeck = studyDeck;
            return this;
        }

        public StudyDocumentBuilder questions(final List<Question> questions) {
            this.questions = questions;
            return this;
        }

        public StudyDocumentBuilder question(final Question question) {
            this.questions.add(question);
            return this;
        }

        public StudyDocumentBuilder createdAt(final OffsetDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public StudyDocumentBuilder updatedAt(final OffsetDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public StudyDocument build() {
            var rightQuestions = questions.stream().filter(Question::isCorrect).toList();
            var complete = rightQuestions.size() == studyDeck.cards().size();
            return new StudyDocument(id, userId, complete, studyDeck, questions, createdAt, updatedAt);
        }

    }
}
