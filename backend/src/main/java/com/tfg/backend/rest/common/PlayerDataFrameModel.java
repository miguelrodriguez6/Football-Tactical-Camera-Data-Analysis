package com.tfg.backend.rest.common;

public class PlayerDataFrameModel {
    private int optaId;
    private int frameIdx;
    private Double x;
    private Double y;
    private int team;
    private int number;

    public PlayerDataFrameModel(int optaId, int frameIdx, Double x, Double y, int team, int number) {
        this.optaId = optaId;
        this.frameIdx = frameIdx;
        this.x = x;
        this.y = y;
        this.team = team;
        this.number = number;
    }

    public int getOptaId() {
        return optaId;
    }

    public void setOptaId(int optaId) {
        this.optaId = optaId;
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

    public int getTeam() {
        return team;
    }

    public void setTeam(int team) {
        this.team = team;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
