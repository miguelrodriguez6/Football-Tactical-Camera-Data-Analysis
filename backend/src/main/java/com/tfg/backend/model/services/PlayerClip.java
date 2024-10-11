package com.tfg.backend.model.services;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PlayerClip {

    private int start;
    private int end;
    private int player;
    private String name;

    PlayerClip(){}

    public PlayerClip(int start, int end, int player, String name){
        this.start = start;
        this.end = end;
        this.player = player;
        this.name = name;
    }

}
