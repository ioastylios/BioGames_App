package com.example.quizgame.datamodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WalkGames {
    @SerializedName("walkGames")
    @Expose
    private List<WalkGame> walkGames = null;

    public List<WalkGame> getQuizes() {
        return walkGames;
    }
    public void setQuizes(List<WalkGame> games) {
        this.walkGames = games;
    }

    public WalkGame getQuiz(String QuizType,String QuizDiff) {
        WalkGame quiz = null;
        for(int i = 0;i< getQuizes().size();i++){
            quiz = walkGames.get(i);
            String quizType = quiz.getQuizType();
            String quizDifficulty = quiz.getQuizDifficulty();
            if(quizType.equalsIgnoreCase(QuizType) && quizDifficulty.equalsIgnoreCase(QuizDiff)){
                break;
            }
        }
        return quiz;
    }
}
