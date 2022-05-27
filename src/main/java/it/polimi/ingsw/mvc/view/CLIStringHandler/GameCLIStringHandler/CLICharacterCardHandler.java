package it.polimi.ingsw.mvc.view.CLIStringHandler.GameCLIStringHandler;

import it.polimi.ingsw.cards.characters.BardCharacter.BardCharacter;
import it.polimi.ingsw.cards.characters.CharacterCard;
import it.polimi.ingsw.cards.characters.FlagCharacter.FlagCharacter;
import it.polimi.ingsw.cards.characters.HerbalistCharacter.HerbalistCharacter;
import it.polimi.ingsw.cards.characters.JokerCharacter.JokerCharacter;
import it.polimi.ingsw.cards.characters.MushroomCharacter.MushroomCharacter;
import it.polimi.ingsw.cards.characters.PipeCharacter.PipeCharacter;
import it.polimi.ingsw.cards.characters.PrincessCharacter.PrincessCharacter;
import it.polimi.ingsw.cards.characters.WineCharacter.WineCharacter;
import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.exceptions.ClientSideCheckException;
import it.polimi.ingsw.messages.ClientMessage;
import it.polimi.ingsw.messages.game.PlayCharacterCardMessage;
import it.polimi.ingsw.mvc.view.CLI.CLIView;
import it.polimi.ingsw.mvc.view.CLIStringHandler.CLIEmptyHandler;
import it.polimi.ingsw.mvc.view.CLIStringHandler.GameCLIStringHandler.ActionHandler.CLIMoveStudentHandler;
import it.polimi.ingsw.mvc.view.CLIStringHandler.GameCLIStringHandler.CharacterArgumentHandler.*;

import java.util.ArrayList;
import java.util.List;

public class CLICharacterCardHandler implements GameCLIStringHandler {
    protected int cardIndex;

    @Override
    public ClientMessage generateMessageFromInput(CLIView cliView, String input) throws ClientSideCheckException {

        checkForExit(input, cliView);

        try {
            cardIndex = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            throw new ClientSideCheckException("Invalid number");
        }
        if (cardIndex < 1 || cardIndex > 3) {
            throw new ClientSideCheckException("Invalid Character Card number. Please chose one from 1 to 3");
        }

        cardIndex = cardIndex - 1;

        // TODO map
        Class<? extends CharacterCard> characterClass = cliView.getCurrentGameCards().get(cardIndex).getClass();
        if (BardCharacter.class.equals(characterClass)) {
            cliView.setCurrentQueryMessage("""
                    Choose up to two students to exchange between your Entrance and your dining room.
                                            
                    Type: <students_entrance> <students_dining>
                                            
                    Where:
                    <students_entrance> is the color (or the colors) of the students in your entrance you want to exchange.
                    <students_dining> is the color (or the colors) of the students in your dining room you want to exchange.
                    color = yellow | blue | green | red | pink
                    
                    Type 'exit' if you have changed your mind.
                    """);

            cliView.setCliStringHandler(new CLIBardCharacterArgumentHandler());
        } else if (JokerCharacter.class.equals(characterClass)) {
            cliView.setCurrentQueryMessage("""
                    Choose up to three students to exchange between the JokerCharacter card and your entrance.
                                            
                    Type: <students_joker> <students_entrance>
                                            
                    Where:
                    <students_joker> is the color (or the colors) of the students in the joker card you want to exchange.
                    <students_entrance> is the color (or the colors) of the students in your entrance you want to exchange.
                    color = yellow | blue | green | red | pink
                    
                    Type 'exit' if you have changed your mind.
                    """);

            cliView.setCliStringHandler(new CLIJokerCharacterArgumentHandler());
        } else if (WineCharacter.class.equals(characterClass)) {
            cliView.setCurrentQueryMessage("""
                    Choose one student from the WineCharacter card and place it on an island of your choice.
                                            
                    Type: <chosen_student> <chosen_island>
                    
                    Where:
                    <chosen_student> is the color of the chosen student.
                    color = yellow | blue | green | red | pink
                    <chosen_island> is the index of the chosen island.
                    
                    Type 'exit' if you have changed your mind.
                    """);

            cliView.setCliStringHandler(new CLIWineCharacterArgumentHandler());
        } else if (FlagCharacter.class.equals(characterClass)) {
            cliView.setCurrentQueryMessage("""
                    Choose an island which will be resolved as if Mother Nature has ended her movement there.
                                            
                    Type: <chosen_island>
                    
                    Where:
                    <chosen_island> is the index of the chosen island.
                    
                    Type 'exit' if you have changed your mind.
                    """);

            cliView.setCliStringHandler(new CLIFlagCharacterArgumentHandler());
        } else if (HerbalistCharacter.class.equals(characterClass)) {
            //TODO
        } else if (MushroomCharacter.class.equals(characterClass)) {
            cliView.setCurrentQueryMessage("""
                    Choose a color of student.
                    During the influence calculation this turn, that color add no influence.
                                            
                    Type: <color>
                    
                    Where:
                    <color> = yellow | blue | green | red | pink
                    
                    Type 'exit' if you have changed your mind.
                    """);

            cliView.setCliStringHandler(new CLIColorCharacterArgumentHandler());
        } else if (PipeCharacter.class.equals(characterClass)) {
            cliView.setCurrentQueryMessage("""
                    Choose a color of student.
                    Every player including yourself must return 3 students of this color from their dining room to the bag.
                    If any player has fewer than 3 students of that color, return as many students they have.
                                            
                    Type: <color>
                                        
                    Where:
                    <color> = yellow | blue | green | red | pink
                                        
                    Type 'exit' if you have changed your mind.
                    """);

            cliView.setCliStringHandler(new CLIColorCharacterArgumentHandler());
        } else if (PrincessCharacter.class.equals(characterClass)) {
            cliView.setCurrentQueryMessage("""
                    Choose a student from this card and place it in your dining room.
                                            
                    Type: <chosen_student>
                    
                    Where:
                    <chosen_student> is the color of the chosen student.
                    color = yellow | blue | green | red | pink
                                        
                    Type 'exit' if you have changed your mind.
                    """);

            cliView.setCliStringHandler(new CLIColorCharacterArgumentHandler());
        } else {
            cliView.setFrontEnd(characterClass.getSimpleName() + " played.");
            restoreCLIView(cliView);
            return new PlayCharacterCardMessage(this.cardIndex, null);
        }
        throw new ClientSideCheckException("");
    }

    protected void restoreCLIView(CLIView cliView) {
        cliView.setCliStringHandler(new CLIEmptyHandler());
    }

    protected void checkForExit(String input, CLIView cliView) throws ClientSideCheckException {
        if(input.trim().equals("exit")) {
            restoreCLIView(cliView);
            throw new ClientSideCheckException("");
        }
    }

    protected List<List<Color>> generateListsOfColors (String[] words) throws ClientSideCheckException {
        List<List<Color>> listsOfColors = new ArrayList<>();

        List<Color> color1 = new ArrayList<>();
        List<Color> color2 = new ArrayList<>();
        int i = 0;
        for (; i < words.length / 2; i++) {
            color1.add(CLIMoveStudentHandler.parseColorString(words[i]));
        }
        for (; i < words.length; i++) {
            color2.add(CLIMoveStudentHandler.parseColorString(words[i]));
        }
        listsOfColors.add(color1);
        listsOfColors.add(color2);
        return listsOfColors;
    }
}
