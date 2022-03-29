package it.polimi.ingsw.cards.characters;

import it.polimi.ingsw.exceptions.NotFoundException;
import it.polimi.ingsw.mvc.model.Model;
import it.polimi.ingsw.player.DiningRoomFullException;

abstract public class CharacterCard {
    protected final Model model;
    private int coinCost;

    public CharacterCard(Model model, int coinCost) {
        this.model = model;
    }

    public int getCoinCost() {
        return coinCost;
    }

    public void activateEffect(Object arguments) throws CCArgumentException {
        internalActivateEffect(arguments);
        coinCost++;
    }


    protected abstract void internalActivateEffect(Object arguments) throws CCArgumentException;
}
