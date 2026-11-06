
// src/main/java/com/unicauca/edu/bancopreguntasparcial2/infrastructure/jpa/record/QuestionRecord.java
package com.unicauca.edu.bancopreguntasparcial2.infrastructure.jpa.record;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "questions")
public class QuestionRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String text;
    private String justification;
    private String state;
    private String correctOption;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "question_id")
    private List<AnswerOptionRecord> options;

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
    public String getJustification() { return justification; }
    public void setJustification(String justification) { this.justification = justification; }
    public String getState() { return state; }
    public void setState(String state) { this.state = state; }
    public String getCorrectOption() { return correctOption; }
    public void setCorrectOption(String correctOption) { this.correctOption = correctOption; }
    public List<AnswerOptionRecord> getOptions() { return options; }
    public void setOptions(List<AnswerOptionRecord> options) { this.options = options; }
}
