package com.unicauca.edu.bancopreguntasparcial2.infrastructure.jpa.mapper;

import com.unicauca.edu.bancopreguntasparcial2.domain.model.DraftState;
import com.unicauca.edu.bancopreguntasparcial2.domain.model.IQuestionState;
import com.unicauca.edu.bancopreguntasparcial2.domain.model.PendingReviewState;
import com.unicauca.edu.bancopreguntasparcial2.domain.model.Question;
import com.unicauca.edu.bancopreguntasparcial2.infrastructure.jpa.record.AnswerOptionRecord;
import com.unicauca.edu.bancopreguntasparcial2.infrastructure.jpa.record.QuestionRecord;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class QuestionDomainMapper {

    private QuestionDomainMapper() {
    }

    public static QuestionRecord toRecord(Question question) {
        if (question == null) {
            return null;
        }

        QuestionRecord record = new QuestionRecord();
        record.setId(question.getId());
        record.setText(question.getText());
        record.setJustification(question.getJustification());
        record.setState(question.getState() != null ? question.getState().getValue() : new DraftState().getValue());
        record.setCorrectOption(question.getCorrectOption());

        if (question.getOptions() != null) {
            List<AnswerOptionRecord> options = question.getOptions().stream()
                    .map(optionText -> {
                        AnswerOptionRecord optionRecord = new AnswerOptionRecord();
                        optionRecord.setOptionText(optionText);
                        return optionRecord;
                    })
                    .collect(Collectors.toList());
            record.setOptions(options);
        }

        return record;
    }

    public static Question toDomain(QuestionRecord record) {
        if (record == null) {
            return null;
        }

        List<String> options = new ArrayList<>();
        if (record.getOptions() != null) {
            options = record.getOptions().stream()
                    .map(AnswerOptionRecord::getOptionText)
                    .collect(Collectors.toList());
        }

        IQuestionState state = parseState(record.getState());

        return new Question.QuestionBuilder()
                .id(record.getId())
                .text(record.getText())
                .justification(record.getJustification())
                .options(options)
                .correctOption(record.getCorrectOption())
                .state(state)
                .build();
    }

    private static IQuestionState parseState(String stateValue) {
        if (stateValue == null) {
            return new DraftState();
        }

        return switch (stateValue) {
            case "DRAFT" -> new DraftState();
            case "PENDING_REVIEW" -> new PendingReviewState();
            default -> new DraftState();
        };
    }
}
