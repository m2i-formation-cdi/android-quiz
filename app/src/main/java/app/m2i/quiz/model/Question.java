package app.m2i.quiz.model;

import java.util.UUID;

public class Question {
    private String text;
    private UUID id;
    private Boolean goodAnswer;
    private Boolean userAnswer;

    public Question() {
    }

    public Question(String text, Boolean goodAnswer) {
        this.text = text;
        this.goodAnswer = goodAnswer;
        this.id = UUID.randomUUID();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Boolean getGoodAnswer() {
        return goodAnswer;
    }

    public void setGoodAnswer(Boolean goodAnswer) {
        this.goodAnswer = goodAnswer;
    }

    public Boolean getUserAnswer() {
        return userAnswer;
    }

    public void setUserAnswer(Boolean userAnswer) {
        this.userAnswer = userAnswer;
    }

    public boolean isAnswered(){
        return userAnswer != null;
    }
}
