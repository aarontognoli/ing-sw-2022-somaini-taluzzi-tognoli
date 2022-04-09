package it.polimi.ingsw.cards.characters;

import java.lang.reflect.InvocationTargetException;
import java.util.Random;

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

    private final Class<? extends CharacterCard>[] cardsConstructors;
    private final Random random;

    public CharacterFactory() {
        cardsConstructors = new Class[] {
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

        random = new Random();
    }

    /**
     * @param model reference to the model
     * @return a new random CharacterCard without repetitions for this instance
     */
    public CharacterCard getRandomCharacter(Model model) {
        int index;
        Class<? extends CharacterCard> thisClass;

        do {
            index = random.nextInt(cardsConstructors.length);
            thisClass = cardsConstructors[index];
        } while(thisClass == null);

        // Set this constructor as null, so that we can avoid repetitions
        cardsConstructors[index] = null;

        try {
            // Get the constructor with Model as a parameter, then instantiate it using
            // model as argument
            return thisClass.getDeclaredConstructor(Model.class).newInstance(model);
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
            throw new RuntimeException("Unable to create character card (?). " + e.getMessage());
        }
    }
}
