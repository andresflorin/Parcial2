package com.unicauca.edu.bancopreguntasparcial2.infrastructure.api;

import java.util.List;

public class QuestionRequestDTO {
    private String text;
    private String justification;
    private List<String> options;
    private String correctOption;

    // Getters y Setters
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
    public String getJustification() { return justification; }
    public void setJustification(String justification) { this.justification = justification; }
    public List<String> getOptions() { return options; }
    public void setOptions(List<String> options) { this.options = options; }
    public String getCorrectOption() { return correctOption; }
    public void setCorrectOption(String correctOption) { this.correctOption = correctOption; }
}
