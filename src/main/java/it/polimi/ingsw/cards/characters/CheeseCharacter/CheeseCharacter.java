package it.polimi.ingsw.cards.characters.CheeseCharacter;

import it.polimi.ingsw.cards.characters.CCArgumentException;
import it.polimi.ingsw.cards.characters.CharacterCard;
import it.polimi.ingsw.enums.CharacterCardsEffectArguments;
import it.polimi.ingsw.mvc.model.Model;
import it.polimi.ingsw.mvc.model.ProfessorMoverRuleCheese;

public class CheeseCharacter extends CharacterCard {

    public CheeseCharacter(Model fatherModel) {
        super(fatherModel, 2);
        super.argumentType = CharacterCardsEffectArguments.NONE;
    }

    @Override
    protected void internalActivateEffect(Object arguments) throws CCArgumentException {
        if (arguments != null) {
            throw new CCArgumentException(CCArgumentException.INVALID_CLASS_MESSAGE);
        }

        String targetPlayerName = model.publicModel.getCurrentPlayer().getNickname();

        model.characterModel.updateProfessorMoverRule(new ProfessorMoverRuleCheese(targetPlayerName));
    }
}
