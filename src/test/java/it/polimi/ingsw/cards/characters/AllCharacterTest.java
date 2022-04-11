package it.polimi.ingsw.cards.characters;

import it.polimi.ingsw.exceptions.InsufficientCoinException;
import it.polimi.ingsw.mvc.model.Model;
import it.polimi.ingsw.mvc.model.PublicModelTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class AllCharacterTest {

    protected Model model;
    protected final Class<? extends CharacterCard> characterCardClass;

    protected AllCharacterTest(Class<? extends CharacterCard> cardClass) {
        characterCardClass = cardClass;
    }

    @BeforeEach
    void setUp() {
        model = PublicModelTest.twoPlayersExpertMode();
        try {
            // Instantiate the character card using the class passed to the constructor
            CharacterCard card = characterCardClass.getDeclaredConstructor(Model.class).newInstance(model);
            PublicModelTest.setupPlayCharacterCard(model, card);
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
            throw new RuntimeException("Unable to create character card (?). " + e.getMessage());
        }
    }

    @Test
    void invalidArgument() {
        try {
            model.publicModel.playCharacterCard(0, new Object());
            assert false;
        } catch (InsufficientCoinException e) {
            assert false;
        } catch (CCArgumentException e) {
            assertEquals(CCArgumentException.INVALID_CLASS_MESSAGE, e.getMessage());
        }
    }
}