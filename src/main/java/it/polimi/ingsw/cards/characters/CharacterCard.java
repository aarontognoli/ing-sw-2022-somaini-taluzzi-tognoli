package it.polimi.ingsw.cards.characters;

import it.polimi.ingsw.mvc.model.Model;

import java.io.Serializable;

abstract public class CharacterCard implements Serializable {
    protected final Model model;
    private int coinCost;

    public CharacterCard(Model model, int coinCost) {
        this.model = model;
        this.coinCost = coinCost;
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
