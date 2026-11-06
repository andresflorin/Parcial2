package com.unicauca.edu.bancopreguntasparcial2.domain.model;

import java.util.List;

public class Question {
    private Long id;
    private String text;
    private String justification;
    private List<String> options;
    private String correctOption;
    private IQuestionState state;

    private Question(Long id, String text, String justification, List<String> options, String correctOption, IQuestionState state) {
        this.id = id;
        this.text = text;
        this.justification = justification;
        this.options = options;
        this.correctOption = correctOption;
        this.state = state;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getText() { return text; }
    public String getJustification() { return justification; }
    public List<String> getOptions() { return options; }
    public String getCorrectOption() { return correctOption; }
    public IQuestionState getState() { return state; }
    public void setState(IQuestionState state) { this.state = state; }

    public static class QuestionBuilder implements IQuestionBuilder {
        private Long id;
        private String text;
        private String justification;
        private List<String> options;
        private String correctOption;
        private IQuestionState state = new DraftState();

        public QuestionBuilder id(Long id) { this.id = id; return this; }
        public QuestionBuilder text(String text) { this.text = text; return this; }
        public QuestionBuilder justification(String justification) { this.justification = justification; return this; }
        public QuestionBuilder options(List<String> options) { this.options = options; return this; }
        public QuestionBuilder correctOption(String correctOption) { this.correctOption = correctOption; return this; }
        public QuestionBuilder state(IQuestionState state) { this.state = state; return this; }

        @Override
        public Question build() {
            if (this.options == null || this.options.size() != 4) {
                throw new IllegalStateException("La pregunta debe tener exactamente 4 opciones de respuesta.");
            }
            if (!this.options.contains(this.correctOption)) {
                throw new IllegalStateException("La respuesta correcta debe estar incluida en las opciones.");
            }
            return new Question(id, text, justification, options, correctOption, state);
        }
    }
}
