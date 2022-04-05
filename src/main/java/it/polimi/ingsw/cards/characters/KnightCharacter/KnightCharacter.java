package it.polimi.ingsw.cards.characters.KnightCharacter;

import it.polimi.ingsw.cards.characters.CCArgumentException;
import it.polimi.ingsw.cards.characters.CharacterCard;
import it.polimi.ingsw.mvc.model.InfluenceCalculatorCentaur;
import it.polimi.ingsw.mvc.model.InfluenceCalculatorKnight;
import it.polimi.ingsw.mvc.model.Model;

public class KnightCharacter extends CharacterCard {

    public KnightCharacter(Model model) {
        super(model, 2);
    }

    @Override
    protected void internalActivateEffect(Object arguments) throws CCArgumentException {
        if (arguments instanceof Integer) {
            throw new CCArgumentException(CCArgumentException.INVALID_CLASS_MESSAGE);
        }

        int targetPlayerIndex = (int) arguments;

        model.characterModel.updateInfluenceCalculator(new InfluenceCalculatorKnight(model,targetPlayerIndex));
    }
}
