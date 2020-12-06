package com.example.quizgame.datamodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Question {

    @SerializedName("questionStatement")
    @Expose
    private String questionStatement;

    @SerializedName("answer")
    @Expose
    private String answer;

    public String getQuestionStatement() {
        return questionStatement;
    }

    public void setQuestionStatement(String questionStatement) { this.questionStatement = questionStatement; }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

}