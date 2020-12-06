package com.example.quizgame.model;

public class WalkGesture {
    public String strType;
    public float x;
    public float y;
    public float z;
    public long timestamp;

    public String toString() {
        return "type:" + strType + " time:" + timestamp  + " x:" + x + " y:" + y + " z:" + z;
    }
}
