package it.polimi.ingsw.mvc.view.CLI;

import it.polimi.ingsw.exceptions.ClientSideCheckException;
import it.polimi.ingsw.messages.ErrorMessage;
import it.polimi.ingsw.messages.ServerMessage;
import it.polimi.ingsw.messages.lobby.client.lobbysetup.RequestLobbyNamesListMessage;
import it.polimi.ingsw.messages.lobby.server.GameStartMessage;
import it.polimi.ingsw.messages.lobby.server.ServerLobbyMessage;
import it.polimi.ingsw.mvc.model.Model;
import it.polimi.ingsw.mvc.view.CLIStringHandler.CLIStringHandler;
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

    private Thread readInputThread;

    public CLIView(Notifier<ServerMessage> messageNotifier, Notifier<Model> modelNotifier) {
        super(messageNotifier, modelNotifier);
    }

    public void show() {
        System.out.println(frontEnd);
        System.out.println(currentQueryMessage);
    }

    public void showModel() {
        // TODO: Draw on screen the updated model
        System.out.println(model);
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
        if (newMessage instanceof ErrorMessage errorMessage) {
            setFrontEnd(errorMessage.getErrorMessageString());
            show();
            return;
        }
        if (newMessage instanceof GameStartMessage gameStartMessage) {
            setFrontEnd("Loading first model...");
            setCurrentQueryMessage("");
            show();
            model = gameStartMessage.getFirstModel();
            showModel();
            gameStartMessage.updateCLI(this);
            return;
        }
        if (newMessage instanceof ServerLobbyMessage serverLobbyMessage) {
            serverLobbyMessage.updateCLI(this);
            show();
            return;
        }
        // TODO: if message instance of ServerGameMessage ...


    }

    private void asyncReadStdin() {
        readInputThread = new Thread(() -> {
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

    public void setCliStringHandler(CLIStringHandler cliStringHandler) {
        this.cliStringHandler = cliStringHandler;
    }
}
