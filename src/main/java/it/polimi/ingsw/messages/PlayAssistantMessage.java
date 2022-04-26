package it.polimi.ingsw.messages;

import it.polimi.ingsw.cards.assistant.AssistantCard;
import it.polimi.ingsw.mvc.controller.ServerController;

public class PlayAssistantMessage extends Message {

    final private AssistantCard assistantCard;

    public PlayAssistantMessage(AssistantCard assistantCard) {
        this.assistantCard = assistantCard;
    }

    @Override
    public void controllerCallback(ServerController controller) throws Exception {
        controller.playAssistant(assistantCard);
    }
}
