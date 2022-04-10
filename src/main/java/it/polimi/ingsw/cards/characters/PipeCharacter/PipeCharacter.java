package it.polimi.ingsw.cards.characters.PipeCharacter;

import it.polimi.ingsw.cards.characters.CCArgumentException;
import it.polimi.ingsw.cards.characters.CharacterCard;
import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.mvc.model.Model;

public class PipeCharacter extends CharacterCard {
    public PipeCharacter(Model model) {
        super(model, 3);
    }

    @Override
    protected void internalActivateEffect(Object arguments) throws CCArgumentException {
        if (!(arguments instanceof Color)) {
            throw new CCArgumentException(CCArgumentException.INVALID_CLASS_MESSAGE);
        }
        Color whichColor = (Color) arguments;
        try {
            model.characterModel.removeStudentsFromAllBoards(3, whichColor);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("This should never happen");
        }

    }
}
