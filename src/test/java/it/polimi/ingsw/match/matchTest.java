package it.polimi.ingsw.match;

import it.polimi.ingsw.cards.assistant.AssistantCard;
import it.polimi.ingsw.exceptions.PlayerAlreadyChosenAssistantCard;
import it.polimi.ingsw.mvc.model.Model;
import it.polimi.ingsw.player.Player;
import org.junit.jupiter.api.Test;

import static it.polimi.ingsw.mvc.model.PublicModelTest.twoPlayersBasicSetup;
import static org.junit.jupiter.api.Assertions.*;

public class matchTest {
    @Test
    public void twoPlayersEasyMatchTest() {
        Model model = twoPlayersBasicSetup();
        //SAME DECKNAME CHECK IN CONTROLLER(?)

        assertDoesNotThrow(() -> model.publicModel.playAssistant(AssistantCard.CARD_2));//(2 turn, 1 movement)
        model.publicModel.endTurn();
        Player p1 = model.publicModel.getCurrentPlayer();
        assertThrows(PlayerAlreadyChosenAssistantCard.class, () -> model.publicModel.playAssistant(AssistantCard.CARD_2));
        assertDoesNotThrow(() -> model.publicModel.playAssistant(AssistantCard.CARD_1));//(1 turn, 1 movement)
        model.publicModel.endTurn();
        assertEquals(p1, model.publicModel.getCurrentPlayer());
        //TODO


    }


}
