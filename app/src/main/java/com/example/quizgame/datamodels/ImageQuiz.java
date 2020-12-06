package com.example.quizgame.datamodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ImageQuiz {
    @SerializedName("quizDifficulty")
    @Expose
    private String quizDifficulty;

    @SerializedName("quizType")
    @Expose
    private String quizType;

    @SerializedName("noOfImgs")
    @Expose
    private int noOfImgs;

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

    public int getNoOfImgs() {
        return noOfImgs;
    }

    public void setNoOfImgs(int noOfImgs) {
        this.noOfImgs = noOfImgs;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
