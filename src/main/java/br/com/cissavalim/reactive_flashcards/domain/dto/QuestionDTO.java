package br.com.cissavalim.reactive_flashcards.domain.dto;

import io.micrometer.common.util.StringUtils;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.Objects;

public record QuestionDTO(
        String asked,
        OffsetDateTime askedAt,
        String answered,
        OffsetDateTime answeredAt,
        String expected
) {

    public Boolean isAnswered() {
        return Objects.nonNull(answeredAt);
    }

    public Boolean isNotAnswered() {
        return !isAnswered();
    }

    public Boolean isCorrect() {
        return isAnswered() && asked.equalsIgnoreCase(expected);
    }

    public static QuestionDTOBuilder builder() {
        return new QuestionDTOBuilder();
    }

    public QuestionDTOBuilder toBuilder() {
        return new QuestionDTOBuilder(
                asked,
                askedAt,
                answered,
                answeredAt,
                expected
        );
    }

    @NoArgsConstructor
    @AllArgsConstructor
    public static class QuestionDTOBuilder {
        private String asked;
        private OffsetDateTime askedAt;
        private String answered;
        private OffsetDateTime answeredAt;
        private String expected;

        public QuestionDTOBuilder asked(final String asked) {
            if (StringUtils.isNotBlank(asked)) {
                this.asked = asked;
                this.askedAt = OffsetDateTime.now();
            }
            return this;
        }

        public QuestionDTOBuilder answered(final String answered) {
            if (StringUtils.isNotBlank(asked)) {
                this.answered = answered;
                this.answeredAt = OffsetDateTime.now();
            }
            return this;
        }

        public QuestionDTOBuilder expected(final String expected) {
            this.expected = expected;
            return this;
        }

        public QuestionDTO build() {
            return new QuestionDTO(asked, askedAt, answered, answeredAt, expected);
        }
    }
}
