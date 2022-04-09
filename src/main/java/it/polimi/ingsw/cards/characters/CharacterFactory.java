package it.polimi.ingsw.cards.characters;

import java.lang.reflect.InvocationTargetException;

import it.polimi.ingsw.cards.characters.BardCharacter.BardCharacter;
import it.polimi.ingsw.cards.characters.CentaurCharacter.CentaurCharacter;
import it.polimi.ingsw.cards.characters.CheeseCharacter.CheeseCharacter;
import it.polimi.ingsw.cards.characters.FlagCharacter.FlagCharacter;
import it.polimi.ingsw.cards.characters.HerbalistCharacter.HerbalistCharacter;
import it.polimi.ingsw.cards.characters.JokerCharacter.JokerCharacter;
import it.polimi.ingsw.cards.characters.KnightCharacter.KnightCharacter;
import it.polimi.ingsw.cards.characters.MushroomCharacter.MushroomCharacter;
import it.polimi.ingsw.cards.characters.PipeCharacter.PipeCharacter;
import it.polimi.ingsw.cards.characters.PostManCharacter.PostManCharacter;
import it.polimi.ingsw.cards.characters.PrincessCharacter.PrincessCharacter;
import it.polimi.ingsw.cards.characters.WineCharacter.WineCharacter;
import it.polimi.ingsw.mvc.model.Model;

public class CharacterFactory {

    private static final Class<? extends CharacterCard>[] CARDS_CONSTRUCTORS = new Class[] {
            BardCharacter.class,
            CentaurCharacter.class,
            CheeseCharacter.class,
            FlagCharacter.class,
            HerbalistCharacter.class,
            JokerCharacter.class,
            KnightCharacter.class,
            MushroomCharacter.class,
            PipeCharacter.class,
            PostManCharacter.class,
            PrincessCharacter.class,
            WineCharacter.class
    };

    public static CharacterCard createCharacter(Model model, int index) {
        Class<? extends CharacterCard> thisClass = CARDS_CONSTRUCTORS[index];
        try {
            // Get the constructor with Model as a parameter, then instanciate it using
            // model as argument
            return (CharacterCard) thisClass.getDeclaredConstructor(Model.class).newInstance(model);
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
            throw new RuntimeException("Unable to create character card (?). " + e.getMessage());
        }
    }
}
