package it.polimi.ingsw.match;

import it.polimi.ingsw.cards.assistant.AssistantCard;
import it.polimi.ingsw.mvc.model.Model;
import org.junit.jupiter.api.Test;

import static it.polimi.ingsw.mvc.model.PublicModelTest.twoPlayersBasicSetup;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class matchTest {
    @Test
    public void twoPlayersEasyMatchTest() {
        Model model = twoPlayersBasicSetup();
        //SAME DECKNAME CHECK IN CONTROLLER(?)
        assertDoesNotThrow(() -> model.publicModel.playAssistant(AssistantCard.CARD_1));
        model.publicModel.endTurn();
        //TODO


    }


}
