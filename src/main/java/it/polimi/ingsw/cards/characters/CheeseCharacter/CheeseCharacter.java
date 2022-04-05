package it.polimi.ingsw.cards.characters.CheeseCharacter;

import it.polimi.ingsw.cards.characters.CCArgumentException;
import it.polimi.ingsw.cards.characters.CharacterCard;
import it.polimi.ingsw.mvc.model.Model;
import it.polimi.ingsw.mvc.model.ProfessorMoverRuleCheese;

public class CheeseCharacter extends CharacterCard {

    public CheeseCharacter(Model fatherModel) {
        super(fatherModel, 2);
    }

    @Override
    protected void internalActivateEffect(Object arguments) throws CCArgumentException {
        if (arguments.getClass() != String.class) {
            throw new CCArgumentException(CCArgumentException.INVALID_CLASS_MESSAGE);
        }

        String targetPlayerName = (String) arguments;

        model.characterModel.updateProfessorMoverRule(new ProfessorMoverRuleCheese(targetPlayerName));
    }
}
