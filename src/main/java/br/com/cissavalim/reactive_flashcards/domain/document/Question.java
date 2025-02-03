package br.com.cissavalim.reactive_flashcards.domain.document;

import io.micrometer.common.util.StringUtils;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.Objects;

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

    public Boolean isAnswered() {
        return Objects.nonNull(answeredAt);
    }

    public Boolean isNotAnswered() {
        return Objects.isNull(answeredAt);
    }

    public Boolean isCorrect() {
        return isAnswered() && answered.equals(expected);
    }

    public QuestionBuilder toBuilder() {
        return new QuestionBuilder(
                asked,
                askedAt,
                answered,
                answeredAt,
                expected
        );
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
            if (StringUtils.isNotBlank(asked)) {
                this.asked = asked;
                this.askedAt = OffsetDateTime.now();
            }
            return this;
        }

        public QuestionBuilder answered(final String answered) {
            if (StringUtils.isNotBlank(answered)) {
                this.answered = answered;
                this.answeredAt = OffsetDateTime.now();
            }
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
