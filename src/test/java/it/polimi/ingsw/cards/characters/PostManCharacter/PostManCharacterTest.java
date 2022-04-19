package it.polimi.ingsw.cards.characters.PostManCharacter;

import it.polimi.ingsw.cards.assistant.AssistantCard;
import it.polimi.ingsw.cards.characters.AllCharacterTest;
import it.polimi.ingsw.player.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PostManCharacterTest extends AllCharacterTest {

    PostManCharacterTest() {
        super(PostManCharacter.class);
    }

    /**
     * 1. Play assistant card
     * 2. Check max move
     * 3. Play PostManCard
     * 4. Check incremented max move
     */
    @Test
    void normalEffect() {
        Player currentPlayer = model.publicModel.getCurrentPlayer();

        AssistantCard firstCard = currentPlayer.getDeck().getHand().get(0);

        assertDoesNotThrow(() -> currentPlayer.setCurrentAssistantCard(firstCard));
        assertEquals(
                firstCard.getMaxMotherNatureMovementValue(),
                currentPlayer.getMaxMotherNatureMovementValue()
        );

        assertDoesNotThrow(() -> playCard(null));
        assertEquals(
                firstCard.getMaxMotherNatureMovementValue() +
                        PostManCharacter.INCREMENT_EFFECT,
                currentPlayer.getMaxMotherNatureMovementValue()
        );
    }
}