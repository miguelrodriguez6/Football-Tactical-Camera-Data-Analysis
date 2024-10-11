package com.tfg.backend.rest.common;

public class BallDataFrameModel {

    private int frameIdx;
    private Double x;
    private Double y;

    public BallDataFrameModel(int frameIdx, Double x, Double y) {
        this.frameIdx = frameIdx;
        this.x = x;
        this.y = y;
    }

    public int getFrameIdx() {
        return frameIdx;
    }

    public void setFrameIdx(int frameIdx) {
        this.frameIdx = frameIdx;
    }

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }
}
