// src/main/java/com/unicauca/edu/bancopreguntasparcial2/domain/model/PendingReviewState.java
package com.unicauca.edu.bancopreguntasparcial2.domain.model;

public class PendingReviewState implements IQuestionState {
    @Override
    public void next(Question question) {
        // Estado final en el alcance del examen
    }

    @Override
    public String getValue() {
        return "PENDING_REVIEW";
    }
}
