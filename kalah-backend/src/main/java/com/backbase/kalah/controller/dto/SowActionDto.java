package com.backbase.kalah.controller.dto;

import com.backbase.kalah.domain.Game;
import lombok.Data;

@Data
public class SowActionDto {

    private Game.Player player;
    private Integer pitIndex;

}
