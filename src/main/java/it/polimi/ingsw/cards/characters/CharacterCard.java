package it.polimi.ingsw.cards.characters;

import it.polimi.ingsw.mvc.model.Model;

abstract public class CharacterCard {
    final Model model;

    public CharacterCard(Model model) {
        this.model = model;
    }

    public abstract void activateEffect(Object arguments);
}
