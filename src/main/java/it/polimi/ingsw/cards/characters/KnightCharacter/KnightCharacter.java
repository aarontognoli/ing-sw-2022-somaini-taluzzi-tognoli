package it.polimi.ingsw.cards.characters.KnightCharacter;

import it.polimi.ingsw.cards.characters.CCArgumentException;
import it.polimi.ingsw.cards.characters.CharacterCard;
import it.polimi.ingsw.enums.CharacterCardsEffectArguments;
import it.polimi.ingsw.mvc.model.InfluenceCalculatorKnight;
import it.polimi.ingsw.mvc.model.Model;

public class KnightCharacter extends CharacterCard {

    public KnightCharacter(Model model) {
        super(model, 2);
        super.argumentType = CharacterCardsEffectArguments.NONE;
    }

    @Override
    protected void internalActivateEffect(Object arguments) throws CCArgumentException {
        if (arguments != null) {
            throw new CCArgumentException(CCArgumentException.INVALID_CLASS_MESSAGE);
        }

        int targetPlayerIndex = model.publicModel.getPlayers().indexOf(model.publicModel.getCurrentPlayer());

        model.characterModel.updateInfluenceCalculator(new InfluenceCalculatorKnight(model, targetPlayerIndex));
    }
}
