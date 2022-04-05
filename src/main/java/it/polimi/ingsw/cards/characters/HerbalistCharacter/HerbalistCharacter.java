package it.polimi.ingsw.cards.characters.HerbalistCharacter;

import it.polimi.ingsw.cards.characters.CCArgumentException;
import it.polimi.ingsw.cards.characters.CharacterCard;
import it.polimi.ingsw.mvc.model.Model;
import it.polimi.ingsw.places.Island;

public class HerbalistCharacter extends CharacterCard {
    private int activeNoEntryTiles;

    public HerbalistCharacter(Model model) {
        super(model, 2);
        activeNoEntryTiles = 0;
    }

    public void addNoEntryTile() {
        activeNoEntryTiles++;
    }

    @Override
    protected void internalActivateEffect(Object arguments) throws CCArgumentException {
        if (!(arguments instanceof Island)) {
            throw new CCArgumentException(CCArgumentException.INVALID_CLASS_MESSAGE);
        }
        if (activeNoEntryTiles == 4) {
            throw new CCArgumentException("There are no more No Entry tiles to use");
        }
        try {
            ((Island) arguments).putNoEntryTile();
            activeNoEntryTiles++;
        }catch (Exception e) {
            throw new CCArgumentException(e.getMessage());
        }
    }
}
