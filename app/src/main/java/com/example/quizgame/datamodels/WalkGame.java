package com.example.quizgame.datamodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WalkGame {
    @SerializedName("quizDifficulty")
    @Expose
    private String quizDifficulty;

    @SerializedName("quizType")
    @Expose
    private String quizType;

    @SerializedName("noOfSteps")
    @Expose
    private int noOfSteps;

    @SerializedName("time")
    @Expose
    private int time;
    public String getQuizDifficulty() {
        return quizDifficulty;
    }

    public void setQuizDifficulty(String quizDifficulty) {
        this.quizDifficulty = quizDifficulty;
    }

    public String getQuizType() {
        return quizType;
    }

    public void setQuizType(String quizType) {
        this.quizType = quizType;
    }

    public int getNoOfStep() {
        return noOfSteps;
    }

    public void setNoOfSteps(int noOfSteps) {
        this.noOfSteps = noOfSteps;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
