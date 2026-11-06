// src/main/java/com/unicauca/edu/bancopreguntasparcial2/domain/model/IQuestionState.java
package com.unicauca.edu.bancopreguntasparcial2.domain.model;

public interface IQuestionState {
    void next(Question question);
    String getValue();
}
