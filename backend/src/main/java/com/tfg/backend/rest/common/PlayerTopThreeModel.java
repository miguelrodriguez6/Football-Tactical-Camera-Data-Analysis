package com.tfg.backend.rest.common;

public class PlayerTopThreeModel {

    private String name;
    private String stat;
    private String team;

    public PlayerTopThreeModel(String name, String stat, String team) {
        this.name = name;
        this.stat = stat;
        this.team = team;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStat() {
        return stat;
    }

    public void setStat(String stat) {
        this.stat = stat;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }
}
