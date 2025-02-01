package br.com.cissavalim.reactive_flashcards.domain.document;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

public record Question(
        String asked,
        OffsetDateTime askedAt,
        String answered,
        OffsetDateTime answeredAt,
        String expected
) {

    public static QuestionBuilder builder() {
        return new QuestionBuilder();
    }

    public QuestionBuilder toBuilder() {
        return new QuestionBuilder()
                .asked(this.asked)
                .askedAt(this.askedAt)
                .answered(this.answered)
                .answeredAt(this.answeredAt)
                .expected(this.expected);
    }

    @NoArgsConstructor
    @AllArgsConstructor
    public static class QuestionBuilder {
        private String asked;
        private OffsetDateTime askedAt;
        private String answered;
        private OffsetDateTime answeredAt;
        private String expected;

        public QuestionBuilder asked(final String asked) {
            this.asked = asked;
            return this;
        }

        public QuestionBuilder askedAt(final OffsetDateTime askedAt) {
            this.askedAt = askedAt;
            return this;
        }

        public QuestionBuilder answered(final String answered) {
            this.answered = answered;
            return this;
        }

        public QuestionBuilder answeredAt(final OffsetDateTime answeredAt) {
            this.answeredAt = answeredAt;
            return this;
        }

        public QuestionBuilder expected(final String expected) {
            this.expected = expected;
            return this;
        }

        public Question build() {
            return new Question(asked, askedAt, answered, answeredAt, expected);
        }
    }
}
