package br.com.cissavalim.reactive_flashcards.domain.mapper;

import br.com.cissavalim.reactive_flashcards.domain.document.Card;
import br.com.cissavalim.reactive_flashcards.domain.document.Question;
import br.com.cissavalim.reactive_flashcards.domain.document.StudyCard;
import br.com.cissavalim.reactive_flashcards.domain.document.StudyDocument;
import br.com.cissavalim.reactive_flashcards.domain.dto.QuestionDTO;
import br.com.cissavalim.reactive_flashcards.domain.dto.StudyCardDTO;
import br.com.cissavalim.reactive_flashcards.domain.dto.StudyDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface StudyDomainMapper {

    StudyCard toStudyCard(final Card cards);

    default Question generateRandomQuestion(final Set<StudyCard> cards) {
        var values = new ArrayList<>(cards);
        var random = new Random();

        var position = random.nextInt(values.size());

        return toQuestion(values.get(position));
    }

    @Mapping(target = "asked", source = "front")
    @Mapping(target = "answered", ignore = true)
    @Mapping(target = "expected", source = "back")
    Question toQuestion(final StudyCard studyCard);

    @Mapping(target = "asked", source = "front")
    @Mapping(target = "answered", ignore = true)
    @Mapping(target = "expected", source = "back")
    QuestionDTO toQuestion(final StudyCardDTO studyCard);

    default StudyDocument answer(final StudyDocument document, final String answer) {
        var currentQuestion = document.getLastPendingQuestion();
        var questions = document.questions();
        var currentQuestionIndex = questions.indexOf(currentQuestion);

        currentQuestion = currentQuestion.toBuilder()
                .answered(answer)
                .build();

        questions.set(currentQuestionIndex, currentQuestion);
        return document.toBuilder()
                .questions(questions)
                .build();
    }

    @Mapping(target = "question", ignore = true)
    StudyDTO toDTO(final StudyDocument document, final List<String> remainingAskings);

    @Mapping(target = "question", ignore = true)
    StudyDocument toDocument(final StudyDTO studyDTO);
}
