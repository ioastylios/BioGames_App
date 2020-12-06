package com.example.quizgame.model;

public class TouchGesture {
    public float x;
    public float y;
    public long t;
    public float p;
    public String action;
    public String toString() {
        return "action:" + action + " x:" + x + " y:" + y + " t:" + t + " p:" + p;
    }
}
