// src/main/java/com/unicauca/edu/bancopreguntasparcial2/infrastructure/jpa/record/AnswerOptionRecord.java
package com.unicauca.edu.bancopreguntasparcial2.infrastructure.jpa.record;

import jakarta.persistence.*;

@Entity
@Table(name = "answer_options")
public class AnswerOptionRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String optionText;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getOptionText() { return optionText; }
    public void setOptionText(String optionText) { this.optionText = optionText; }
}
