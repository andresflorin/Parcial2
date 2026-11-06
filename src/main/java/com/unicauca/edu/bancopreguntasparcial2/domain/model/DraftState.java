package com.unicauca.edu.bancopreguntasparcial2.domain.model;

public class DraftState implements IQuestionState {
    @Override
    public void next(Question question) {
        question.setState(new PendingReviewState());
    }

    @Override
    public String getValue() {
        return "DRAFT";
    }
}
