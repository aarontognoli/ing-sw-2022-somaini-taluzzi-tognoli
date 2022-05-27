package it.polimi.ingsw.cards.characters.FlagCharacter;

import it.polimi.ingsw.cards.characters.CCArgumentException;
import it.polimi.ingsw.cards.characters.CharacterCard;
import it.polimi.ingsw.mvc.model.Model;
import it.polimi.ingsw.places.Island;

public class FlagCharacter extends CharacterCard {

    public FlagCharacter(Model model) {
        super(model, 3);
    }

    @Override
    public void internalActivateEffect(Object arguments) throws CCArgumentException {
        if (!(arguments instanceof Integer targetIndex)) {
            throw new CCArgumentException(CCArgumentException.INVALID_CLASS_MESSAGE);
        }

        model.publicModel.updateIslandOwner(model.publicModel.getIslands().get(targetIndex));
    }
}
