package it.polimi.ingsw.mvc.view.CLIStringHandler.GameCLIStringHandler.ActionHandler;

import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.exceptions.ClientSideCheckException;
import it.polimi.ingsw.messages.ClientMessage;
import it.polimi.ingsw.messages.game.MoveStudentToDiningRoomMessage;
import it.polimi.ingsw.messages.game.MoveStudentToIslandMessage;
import it.polimi.ingsw.mvc.view.CLI.CLIView;

public class CLIMoveStudentHandler extends CLIActionPhaseHandler {
    @Override
    public ClientMessage generateMessageFromInput(CLIView cliView, String input) throws ClientSideCheckException {
        handleCharacterCardMessage(cliView, input);

        String words[] = input.split(" ");

        if (words.length < 2)
            throw new ClientSideCheckException("Invalid arguments count");

        String colorName = words[0];
        String placeName = words[1];


        colorName = colorName.toLowerCase();

        Color studentColor = switch (colorName) {
            case "yellow" -> Color.YELLOW_GNOMES;
            case "red" -> Color.RED_DRAGONS;
            case "green" -> Color.GREEN_FROGS;
            case "blue" -> Color.BLUE_UNICORNS;
            case "pink" -> Color.PINK_FAIRIES;
            default -> throw new ClientSideCheckException("Invalid Color");
        };

        switch (placeName) {
            case "dining":
                if (words.length != 2)
                    throw new ClientSideCheckException("Invalid arguments count");

                return new MoveStudentToDiningRoomMessage(studentColor);
            case "island":
                if (words.length != 3)
                    throw new ClientSideCheckException("Invalid arguments count");
                try {
                    int islandIndex = Integer.parseInt(words[2]) - 1;
                    if (islandIndex < 0 || islandIndex >= cliView.getIslandCountFromModel()) {
                        throw new ClientSideCheckException("Invalid island number");
                    }

                    return new MoveStudentToIslandMessage(studentColor, islandIndex);
                } catch (NumberFormatException e) {
                    throw new ClientSideCheckException("Invalid island number");
                }
            default:
                throw new ClientSideCheckException("Invalid place name: " + placeName);
        }
    }
}
