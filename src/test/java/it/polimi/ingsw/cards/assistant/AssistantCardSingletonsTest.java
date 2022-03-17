package it.polimi.ingsw.cards.assistant;

import org.junit.Test;

public class AssistantCardSingletonsTest {

    @Test
    public void allEnumsHaveIndex() {
        AssistantCardID[] allEnumsValue = AssistantCardID.class.getEnumConstants();

        for (int i = 0; i < allEnumsValue.length; i++) {
            AssistantCardID currentEnum = allEnumsValue[i];
            AssistantCardSingletons.getCardById(currentEnum);
        }
    }

}