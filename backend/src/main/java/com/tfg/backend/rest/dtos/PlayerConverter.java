package com.tfg.backend.rest.dtos;

import com.tfg.backend.model.entities.Player;

import java.util.List;
import java.util.stream.Collectors;

public class PlayerConverter {

    public PlayerConverter(){}
    public static final PlayerDto toPlayerDto(Player player){
        return new PlayerDto(player.getOptaId(), player.getName(), player.getNumber(), player.getPosition());
    }

    public static final List<PlayerDto> toPlayersDto(List<Player> playerMetadataList){
        return playerMetadataList.stream().map(PlayerConverter::toPlayerDto).collect(Collectors.toList());
    }
}
