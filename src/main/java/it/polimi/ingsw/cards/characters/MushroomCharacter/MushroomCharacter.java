package it.polimi.ingsw.cards.characters.MushroomCharacter;

import it.polimi.ingsw.cards.characters.CCArgumentException;
import it.polimi.ingsw.cards.characters.CharacterCard;
import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.exceptions.ClientSideCheckException;
import it.polimi.ingsw.messages.ClientMessage;
import it.polimi.ingsw.mvc.model.InfluenceCalculatorMushroom;
import it.polimi.ingsw.mvc.model.Model;
import it.polimi.ingsw.mvc.view.CLI.CLIView;
import it.polimi.ingsw.mvc.view.CLIStringHandler.GameCLIStringHandler.CharacterArgumentHandler.CLIColorCharacterArgumentHandler;
import it.polimi.ingsw.mvc.view.GUI.controllers.CardsInfo.CardInfoColor;
import it.polimi.ingsw.mvc.view.GUI.controllers.CardsInfo.CardInfoController;

public class MushroomCharacter extends CharacterCard {
    public MushroomCharacter(Model model) {
        super(model, 3);
    }

    @Override
    public CardInfoController getCharacterCardInfoController() {
        return new CardInfoColor();
    }

    @Override
    public ClientMessage CLIClientSideActivate(CLIView cliView, int cardIndex) throws ClientSideCheckException {
        cliView.setCurrentQueryMessage("""
                Choose a color of student.
                During the influence calculation this turn, that color add no influence.
                                        
                Type: <color>
                                    
                Where:
                <color> = yellow | blue | green | red | pink
                                    
                Type 'exit' if you have changed your mind.
                """);

        cliView.setCliStringHandler(new CLIColorCharacterArgumentHandler(cardIndex));
        throw new ClientSideCheckException();
    }

    @Override
    protected void internalActivateEffect(Object arguments) throws CCArgumentException {
        if (!(arguments instanceof Color targetColor)) {
            throw new CCArgumentException(CCArgumentException.INVALID_CLASS_MESSAGE);
        }

        model.characterModel.updateInfluenceCalculator(new InfluenceCalculatorMushroom(model, targetColor));
    }
}
