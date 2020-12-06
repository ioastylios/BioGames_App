package com.example.quizgame.datamodels;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Quizes {

    @SerializedName("quizes")
    @Expose
    private List<Quiz> quizes = null;

    public List<Quiz> getQuizes() {
        return quizes;
    }

    public void setQuizes(List<Quiz> quizes) {
        this.quizes = quizes;
    }

    public Quiz getQuiz(int i) {
        Quiz quiz;
        if(i >= 0 && i < quizes.size()) {
            quiz = quizes.get(i);
        }else
        {
            quiz = null;
        }
        return quiz;
    }
}