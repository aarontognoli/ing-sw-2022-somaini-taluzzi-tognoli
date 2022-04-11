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

    private int cardsConstructorsSize;

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

        cardsConstructorsSize = cardsConstructors.length;

        random = new Random();
    }

    /**
     * @param model reference to the model
     * @return a new random CharacterCard without repetitions for this instance
     */
    public CharacterCard getRandomCharacter(Model model) {
        int index = random.nextInt(cardsConstructorsSize);
        Class<? extends CharacterCard> thisClass = cardsConstructors[index];

        // Set this constructor as null and swap with last position in array
        cardsConstructors[index] = cardsConstructors[cardsConstructorsSize - 1];
        cardsConstructors[cardsConstructorsSize - 1] = null;

        // Decrement virtual size of array, as we set the last one to null
        cardsConstructorsSize--;

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
