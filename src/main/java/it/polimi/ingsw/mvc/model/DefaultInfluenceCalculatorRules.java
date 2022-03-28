package it.polimi.ingsw.mvc.model;

import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.enums.TowerColor;
import it.polimi.ingsw.places.Island;
import it.polimi.ingsw.player.Board;

public class DefaultInfluenceCalculatorRules extends InfluenceCalculatorRules {

    public DefaultInfluenceCalculatorRules(Model fatherModel) {
        super(fatherModel);
    }

    @Override
    int getTowerInfluence(int playerIndex, Island island) {
        TowerColor islandTowerColor;
        try {
            islandTowerColor = island.getTowerColor();
        } catch (Exception e) {
            // Island contains no tower. Influence is zero.
            return 0;
        }

        TowerColor playerTowerColor = fatherModel.players.get(playerIndex).getTowerColor();

        if (playerTowerColor.equals(islandTowerColor)) {
            return island.getTowers().size();
        } else {
            return 0;
        }
    }

    @Override
    boolean hasProfessorOfColor(int playerIndex, Color color) {
        Board playerBoard = fatherModel.players.get(playerIndex).getBoard();
        Board colorOwnerBoard = fatherModel.privateModel.getProfessorOwner(color);

        return playerBoard.equals(colorOwnerBoard);
    }

    @Override
    int incrementInfluence(int playerIndex) {
        return 0;
    }

}
