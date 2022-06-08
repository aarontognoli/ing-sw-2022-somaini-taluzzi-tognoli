package it.polimi.ingsw.cards.characters.PipeCharacter;

import it.polimi.ingsw.cards.characters.CCArgumentException;
import it.polimi.ingsw.cards.characters.CharacterCard;
import it.polimi.ingsw.enums.CharacterCardsEffectArguments;
import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.exceptions.ClientSideCheckException;
import it.polimi.ingsw.messages.ClientMessage;
import it.polimi.ingsw.mvc.model.Model;
import it.polimi.ingsw.mvc.view.CLI.CLIView;
import it.polimi.ingsw.mvc.view.CLIStringHandler.GameCLIStringHandler.CharacterArgumentHandler.CLIColorCharacterArgumentHandler;

public class PipeCharacter extends CharacterCard {
    public PipeCharacter(Model model) {
        super(model, 3);
        super.argumentType = CharacterCardsEffectArguments.COLOR;
    }

    @Override
    public ClientMessage CLIClientSideActivate(CLIView cliView, int cardIndex) throws ClientSideCheckException {
        cliView.setCurrentQueryMessage("""
                Choose a color of student.
                Every player including yourself must return 3 students of this color from their dining room to the bag.
                If any player has fewer than 3 students of that color, return as many students they have.
                                        
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
        if (!(arguments instanceof Color whichColor)) {
            throw new CCArgumentException(CCArgumentException.INVALID_CLASS_MESSAGE);
        }

        model.characterModel.removeStudentsFromAllBoards(whichColor, 3);
    }
}
