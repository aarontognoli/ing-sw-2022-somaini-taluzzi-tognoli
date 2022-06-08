package it.polimi.ingsw.cards.characters.FlagCharacter;

import it.polimi.ingsw.cards.characters.CCArgumentException;
import it.polimi.ingsw.cards.characters.CharacterCard;
import it.polimi.ingsw.enums.CharacterCardsEffectArguments;
import it.polimi.ingsw.exceptions.ClientSideCheckException;
import it.polimi.ingsw.messages.ClientMessage;
import it.polimi.ingsw.mvc.model.Model;
import it.polimi.ingsw.mvc.view.CLI.CLIView;
import it.polimi.ingsw.mvc.view.CLIStringHandler.GameCLIStringHandler.CharacterArgumentHandler.CLIIslandCharacterArgumentHandler;

public class FlagCharacter extends CharacterCard {

    public FlagCharacter(Model model) {
        super(model, 3);
        super.argumentType = CharacterCardsEffectArguments.ISLAND;
    }

    @Override
    public ClientMessage CLIClientSideActivate(CLIView cliView, int cardIndex) throws ClientSideCheckException {
        cliView.setCurrentQueryMessage("""
                Choose an island which will be resolved as if Mother Nature has ended her movement there.
                                        
                Type: <chosen_island>
                                    
                Where:
                <chosen_island> is the index of the chosen island.
                                    
                Type 'exit' if you have changed your mind.
                """);

        cliView.setCliStringHandler(new CLIIslandCharacterArgumentHandler(cardIndex));
        throw new ClientSideCheckException();
    }

    @Override
    public void internalActivateEffect(Object arguments) throws CCArgumentException {
        if (!(arguments instanceof Integer targetIndex)) {
            throw new CCArgumentException(CCArgumentException.INVALID_CLASS_MESSAGE);
        }

        model.publicModel.updateIslandOwner(model.publicModel.getIslands().get(targetIndex));
    }
}
