package com.example.quizgame.model;

public class KeyGesture {
    public char keyCode;
    public long tp;
    public long ts;
    public float pressure = 1;
    public float posX;
    public float posY;
    public String toString() {
        return "key:" + keyCode + " pressure:" + pressure + " duration:" + tp + " latency:" + ts + " posX:" + posX + " posY:" + posY;
    }
}
