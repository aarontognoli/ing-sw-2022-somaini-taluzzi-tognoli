package it.polimi.ingsw.cards.characters;

import it.polimi.ingsw.mvc.Model;

abstract public class CharacterCard {
    protected final Model model;

    public CharacterCard(Model model) {
        this.model = model;
    }

    public abstract void activateEffect(Object arguments) throws CCArgumentException;
}
