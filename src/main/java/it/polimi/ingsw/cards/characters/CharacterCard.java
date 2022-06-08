package it.polimi.ingsw.cards.characters;

import it.polimi.ingsw.enums.CharacterCardsEffectArguments;
import it.polimi.ingsw.exceptions.ClientSideCheckException;
import it.polimi.ingsw.messages.ClientMessage;
import it.polimi.ingsw.messages.game.PlayCharacterCardMessage;
import it.polimi.ingsw.mvc.model.Model;
import it.polimi.ingsw.mvc.view.CLI.CLIView;
import it.polimi.ingsw.mvc.view.CLIStringHandler.CLIEmptyHandler;

import java.io.Serializable;

abstract public class CharacterCard implements Serializable {
    protected final Model model;
    private int coinCost;
    protected CharacterCardsEffectArguments argumentType;

    public CharacterCard(Model model, int coinCost) {
        this.model = model;
        this.coinCost = coinCost;
    }

    public int getCoinCost() {
        return coinCost;
    }

    public CharacterCardsEffectArguments getArgumentType() {
        return argumentType;
    }

    /**
     * @param arguments argument for this card
     * @throws CCArgumentException invalid argument
     * @implNote this is called server side only
     * @implSpec If the effect is triggered correctly,
     * increment the cost of the card as per the rules
     */
    public void activateEffect(Object arguments) throws CCArgumentException {
        internalActivateEffect(arguments);
        coinCost++;
    }

    public ClientMessage CLIClientSideActivate(CLIView cliView, int cardIndex) throws ClientSideCheckException {
        cliView.setFrontEnd(this.getClass().getSimpleName() + " played.");
        cliView.setCliStringHandler(new CLIEmptyHandler());
        return new PlayCharacterCardMessage(cardIndex, null);
    }

    protected abstract void internalActivateEffect(Object arguments) throws CCArgumentException;
}
