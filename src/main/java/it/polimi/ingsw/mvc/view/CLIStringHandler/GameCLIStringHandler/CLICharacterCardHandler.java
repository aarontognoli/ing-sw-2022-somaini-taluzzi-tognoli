package it.polimi.ingsw.mvc.view.CLIStringHandler.GameCLIStringHandler;

import it.polimi.ingsw.cards.characters.BardCharacter.BardCharacter;
import it.polimi.ingsw.cards.characters.CharacterCard;
import it.polimi.ingsw.cards.characters.JokerCharacter.JokerCharacter;
import it.polimi.ingsw.cards.characters.WineCharacter.WineCharacter;
import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.exceptions.ClientSideCheckException;
import it.polimi.ingsw.messages.ClientMessage;
import it.polimi.ingsw.messages.game.PlayCharacterCardMessage;
import it.polimi.ingsw.mvc.view.CLI.CLIView;
import it.polimi.ingsw.mvc.view.CLIStringHandler.CLIEmptyHandler;
import it.polimi.ingsw.mvc.view.CLIStringHandler.GameCLIStringHandler.CharacterArgumentHandler.CLIBardCharacterArgumentHandler;
import it.polimi.ingsw.mvc.view.CLIStringHandler.GameCLIStringHandler.CharacterArgumentHandler.CLIJokerCharacterArgumentHandler;
import it.polimi.ingsw.mvc.view.CLIStringHandler.GameCLIStringHandler.CharacterArgumentHandler.CLIWineCharacterArgumentHandler;

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
        Class<? extends CharacterCard> characterClass = cliView.getCurrentGameCards().get(cardIndex).getClass();
        if (BardCharacter.class.equals(characterClass)) {
            cliView.setCurrentQueryMessage("""
                    Choose up to two students to exchange between your Entrance and your dining room.
                                            
                    Type: <students_entrance> <students_dining>
                                            
                    Where:
                    <students_entrance> is the color (or the colors) of the students in your entrance you want to exchange.
                    <students_dining> is the color (or the colors) of the students in your dining room you want to exchange.
                    
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
                    
                    Type 'exit' if you have changed your mind.
                    """);

            cliView.setCliStringHandler(new CLIJokerCharacterArgumentHandler());
        } else if (WineCharacter.class.equals(characterClass)) {
            cliView.setCurrentQueryMessage("""
                    Choose one student from the WineCharacter card and place it on an island of your choice.
                                            
                    Type: <chosen_student> <chosen_island>
                    
                    Where:
                    <chosen_student> is the color of the chosen student.
                    <chosen_island> is the index of the chosen island.
                    
                    Type 'exit' if you have changed your mind.
                    """);

            cliView.setCliStringHandler(new CLIWineCharacterArgumentHandler());
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

    protected List<List<Color>> generateListsOfColors (String[] words) throws IllegalArgumentException{
        List<List<Color>> listsOfColors = new ArrayList<>();

        List<Color> color1 = new ArrayList<>();
        List<Color> color2 = new ArrayList<>();
        int i = 0;
        for (; i < words.length / 2; i++) {
            color1.add(Color.valueOf(words[i]));
        }
        for (; i < words.length; i++) {
            color2.add(Color.valueOf(words[1]));
        }
        listsOfColors.add(color1);
        listsOfColors.add(color2);
        return listsOfColors;
    }
}
