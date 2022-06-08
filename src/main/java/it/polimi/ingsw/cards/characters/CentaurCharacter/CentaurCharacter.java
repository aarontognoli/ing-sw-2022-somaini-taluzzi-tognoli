package it.polimi.ingsw.cards.characters.CentaurCharacter;

import it.polimi.ingsw.cards.characters.CCArgumentException;
import it.polimi.ingsw.cards.characters.CharacterCard;
import it.polimi.ingsw.enums.CharacterCardsEffectArguments;
import it.polimi.ingsw.mvc.model.InfluenceCalculatorCentaur;
import it.polimi.ingsw.mvc.model.Model;

public class CentaurCharacter extends CharacterCard {

    public CentaurCharacter(Model model) {
        super(model, 3);
        super.argumentType = CharacterCardsEffectArguments.NONE;
    }

    @Override
    protected void internalActivateEffect(Object arguments) throws CCArgumentException {
        if (arguments != null) {
            throw new CCArgumentException(CCArgumentException.INVALID_CLASS_MESSAGE);
        }
        model.characterModel.updateInfluenceCalculator(new InfluenceCalculatorCentaur(model));
    }
}
