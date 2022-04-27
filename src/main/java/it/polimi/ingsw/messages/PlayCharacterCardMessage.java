package it.polimi.ingsw.messages;

import it.polimi.ingsw.mvc.controller.ServerController;

public class PlayCharacterCardMessage extends Message{
    private final int cardIndex;
    private final Object effectArgument;

    public PlayCharacterCardMessage(int cardIndex, Object effectArgument) {
        this.cardIndex = cardIndex;
        this.effectArgument = effectArgument;
    }


    @Override
    public void controllerCallback(ServerController controller) throws Exception {
        controller.playCharacterCard(cardIndex, effectArgument);
    }
}
