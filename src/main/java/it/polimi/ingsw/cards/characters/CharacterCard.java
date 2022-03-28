package it.polimi.ingsw.cards.characters;

import it.polimi.ingsw.bag.BagEmptyException;
import it.polimi.ingsw.mvc.model.Model;

abstract public class CharacterCard {
    protected final Model model;
    private int coinCost;

    public CharacterCard(Model model, int coinCost) {
        this.model = model;
    }

    public int getCoinCost() {
        return coinCost;
    }

    public void activateEffect(Object arguments) throws CCArgumentException, BagEmptyException {
        internalActivateEffect(arguments);
        coinCost++;
    }


    protected abstract void internalActivateEffect(Object arguments) throws CCArgumentException, BagEmptyException;
}
