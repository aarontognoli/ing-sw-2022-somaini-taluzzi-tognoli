package it.polimi.ingsw.cards.characters.MushroomCharacter;

import it.polimi.ingsw.cards.characters.CCArgumentException;
import it.polimi.ingsw.cards.characters.CharacterCard;
import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.mvc.model.InfluenceCalculatorKnight;
import it.polimi.ingsw.mvc.model.InfluenceCalculatorMushroom;
import it.polimi.ingsw.mvc.model.Model;

public class MushroomCharacter extends CharacterCard {
    public MushroomCharacter(Model model) {
        super(model, 2);
    }

    @Override
    protected void internalActivateEffect(Object arguments) throws CCArgumentException {
        if (!(arguments instanceof Color)) {
            throw new CCArgumentException(CCArgumentException.INVALID_CLASS_MESSAGE);
        }

        Color targetColor = (Color) arguments;

        model.characterModel.updateInfluenceCalculator(new InfluenceCalculatorMushroom(model,targetColor));
    }
}
