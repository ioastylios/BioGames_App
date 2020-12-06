package com.example.quizgame.datamodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ImageQuizes {
    @SerializedName("imageQuizes")
    @Expose
    private List<ImageQuiz> imageQuizes = null;

    public List<ImageQuiz> getQuizes() {
        return imageQuizes;
    }

    public void setQuizes(List<ImageQuiz> quizes) {
        this.imageQuizes = quizes;
    }

    public ImageQuiz getQuiz(String QuizType,String QuizDiff) {
        ImageQuiz quiz = null;
        for(int i = 0;i< getQuizes().size();i++){
            quiz = imageQuizes.get(i);
            String quizType = quiz.getQuizType();
            String quizDifficulty = quiz.getQuizDifficulty();
            if(quizType.equalsIgnoreCase(QuizType) && quizDifficulty.equalsIgnoreCase(QuizDiff)){
                break;
            }
        }
        return quiz;
    }
}
