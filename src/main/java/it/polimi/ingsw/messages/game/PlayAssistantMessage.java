package it.polimi.ingsw.messages.game;

import it.polimi.ingsw.cards.assistant.AssistantCard;
import it.polimi.ingsw.mvc.controller.ServerController;

public class PlayAssistantMessage extends GameMessage {

    final private AssistantCard assistantCard;

    public PlayAssistantMessage(AssistantCard assistantCard) {
        this.assistantCard = assistantCard;
    }

    @Override
    public void controllerCallback(ServerController controller) throws Exception {
        controller.playAssistant(assistantCard);
    }
}
