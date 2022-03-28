package it.polimi.ingsw.cards.characters.HerbalistCharacter;

import it.polimi.ingsw.cards.characters.CCArgumentException;
import it.polimi.ingsw.cards.characters.CharacterCard;
import it.polimi.ingsw.exceptions.NoMoreNoEntryTilesException;
import it.polimi.ingsw.mvc.model.Model;
import it.polimi.ingsw.places.Island;

public class HerbalistCharacter extends CharacterCard {
    private int activeNoEntryTiles;

    public HerbalistCharacter(Model model) {
        super(model, 2);
        activeNoEntryTiles = 0;
    }

    @Override
    protected void internalActivateEffect(Object arguments) throws CCArgumentException, NoMoreNoEntryTilesException {
        if (arguments.getClass() != Island.class) {
            throw new CCArgumentException(CCArgumentException.INVALID_CLASS_MESSAGE);
        }
        if (activeNoEntryTiles == 4) {
            throw new NoMoreNoEntryTilesException();
        }
        ((Island) arguments).putNoEntryTile();
        activeNoEntryTiles++;
    }
}
