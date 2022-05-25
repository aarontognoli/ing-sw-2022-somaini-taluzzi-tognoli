package it.polimi.ingsw.mvc.view.CLI;

import it.polimi.ingsw.enums.GameMode;
import it.polimi.ingsw.enums.GamePhase;
import it.polimi.ingsw.exceptions.ClientSideCheckException;
import it.polimi.ingsw.messages.ServerMessage;
import it.polimi.ingsw.messages.lobby.client.lobbysetup.RequestLobbyNamesListMessage;
import it.polimi.ingsw.mvc.model.CLIModelPrinter;
import it.polimi.ingsw.mvc.model.Model;
import it.polimi.ingsw.mvc.view.CLIStringHandler.CLIEmptyHandler;
import it.polimi.ingsw.mvc.view.CLIStringHandler.CLIStringHandler;
import it.polimi.ingsw.mvc.view.CLIStringHandler.GameCLIStringHandler.ActionHandler.CLIChooseCloudTile;
import it.polimi.ingsw.mvc.view.CLIStringHandler.GameCLIStringHandler.ActionHandler.CLIMoveMotherNature;
import it.polimi.ingsw.mvc.view.CLIStringHandler.GameCLIStringHandler.ActionHandler.CLIMoveStudentHandler;
import it.polimi.ingsw.mvc.view.CLIStringHandler.GameCLIStringHandler.CLICharacterCardHandler;
import it.polimi.ingsw.mvc.view.CLIStringHandler.GameCLIStringHandler.CLIPlayAssistantHandler;
import it.polimi.ingsw.mvc.view.CLIStringHandler.LobbyCLIStringHandler.CLILobbyNameHandler;
import it.polimi.ingsw.mvc.view.ClientView;
import it.polimi.ingsw.notifier.Notifier;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * CLI client View for the lobby
 */
public class CLIView extends ClientView {
    private String frontEnd;
    private String currentQueryMessage;
    private CLIStringHandler cliStringHandler;

    public CLIView(Notifier<ServerMessage> messageNotifier, Notifier<Model> modelNotifier) {
        super(messageNotifier, modelNotifier);
    }

    public void show() {
        if (model != null) {
            showModel();
        }
        System.out.println(frontEnd);
        System.out.println(currentQueryMessage);
    }

    private void planningPhase() {
        currentQueryMessage = "Choose an assistant card from your deck, using a number from 1 to 10";
        cliStringHandler = new CLIPlayAssistantHandler();
    }

    private void actionPhase() {
        // Exception to the normal flow, we want to play a character card
        if (cliStringHandler instanceof CLICharacterCardHandler) {
            return;
        }
        String queryPrefix = model.publicModel.getGameMode().equals(GameMode.EASY_MODE) ? "" :
                "Type 'character' to play a character card\n";

        if (!model.publicModel.enoughStudentsPlaced()) {
            currentQueryMessage = queryPrefix + """
                        Move your students from your entrance. Type:
                        
                        <student_color> <place> <island_number>
                        
                        Where:
                          student_color = yellow | blue | green | red | pink
                          place = dining | island
                          island_number = if place is 'island', this is the number of the chosen island
                    """;
            cliStringHandler = new CLIMoveStudentHandler();

            return;
        }

        if (!model.publicModel.isMotherNatureMoved()) {
            currentQueryMessage = queryPrefix +
                    "Choose the number of steps you want mother nature to be moved, the maximum is " +
                    this.getCurrentPlayerMaxMotherNatureMovement();
            cliStringHandler = new CLIMoveMotherNature();
            return;
        }

        currentQueryMessage = queryPrefix + "Choose the cloud tile to take its students, type the cloud number";
        cliStringHandler = new CLIChooseCloudTile();
    }

    protected void showModel() {
        CLIModelPrinter.printModel(model);

        String currentPlayerNickname = model.publicModel.getCurrentPlayer().getNickname();
        if (!currentPlayerNickname.equals(this.myUsername)) {
            currentQueryMessage = "Wait for your turn. %s is playing.".formatted(currentPlayerNickname);
            cliStringHandler = new CLIEmptyHandler();
            return;
        }

        if (model.publicModel.getGamePhase().equals(GamePhase.PIANIFICATION)) {
            planningPhase();
        } else {
            actionPhase();
        }
    }

    public void setLobbyReloadMessagesAndHandler() {
        frontEnd = "Loading lobbies...";
        currentQueryMessage = "";
        cliStringHandler = new CLILobbyNameHandler();

        show();
    }

    @Override
    public void run() {
        // Query lobbies from server, and set initial message
        setLobbyReloadMessagesAndHandler();

        notifySubscribers(new RequestLobbyNamesListMessage());

        asyncReadStdin();
    }

    @Override
    public void subscribeNotification(ServerMessage newMessage) {
        newMessage.updateCLI(this);
        show();
    }

    private void asyncReadStdin() {
        Thread readInputThread = new Thread(() -> {
            final BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));

            while (true) {
                String newLine;
                try {
                    newLine = stdin.readLine().trim().replaceAll(" +", " ");
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
                try {
                    notifySubscribers(cliStringHandler.generateMessageFromInput(this, newLine));
                } catch (ClientSideCheckException e) {
                    frontEnd = e.getMessage();
                    show();
                }
            }
        });

        readInputThread.start();
    }

    public void setFrontEnd(String frontEnd) {
        this.frontEnd = frontEnd;
    }

    public void setCurrentQueryMessage(String currentQueryMessage) {
        this.currentQueryMessage = currentQueryMessage;
    }

    public String getCurrentQueryMessage() {
        return currentQueryMessage;
    }

    public void setCliStringHandler(CLIStringHandler cliStringHandler) {
        this.cliStringHandler = cliStringHandler;
    }

    public CLIStringHandler getCliStringHandler() {
        return cliStringHandler;
    }
}
