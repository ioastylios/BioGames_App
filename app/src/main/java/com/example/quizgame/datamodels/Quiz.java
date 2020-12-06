package com.example.quizgame.datamodels;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Quiz {
    @SerializedName("quizDifficulty")
    @Expose
    private String quizDifficulty;

    @SerializedName("controlType")
    @Expose
    private String controlType;

    @SerializedName("quizType")
    @Expose
    private String quizType;

    @SerializedName("questions")
    @Expose
    private List<Question> questions = new ArrayList<Question>();

    @SerializedName("quizId")
    @Expose
    private String quizId;

    @SerializedName("time")
    @Expose
    private int time;


    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getQuizDifficulty() { return quizDifficulty; }

    public void setQuizDifficulty(String quizDifficulty) {
        this.quizDifficulty = quizDifficulty;
    }

    public String getControlType() { return controlType; }

    public void setControlType(String controlType) { this.controlType = controlType; }

    public String getQuizType() { return quizType; }

    public void setQuizType(String quizType) { this.quizType = quizType; }

    public String getQuizId() { return quizId; }

    public void setQuizId(String quizId) { this.quizId = quizId; }

}