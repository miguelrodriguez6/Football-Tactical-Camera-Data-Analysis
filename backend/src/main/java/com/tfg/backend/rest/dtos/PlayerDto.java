package com.tfg.backend.rest.dtos;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PlayerDto {

    private int optaId;
    private String name;
    private int number;
    private String position;

    PlayerDto(){}

    public PlayerDto(int optaId, String name, int number, String position) {
        this.optaId = optaId;
        this.name = name;
        this.number = number;
        this.position = position;
    }
}
