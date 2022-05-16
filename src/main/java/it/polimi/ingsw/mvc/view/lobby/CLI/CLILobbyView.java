package it.polimi.ingsw.mvc.view.lobby.CLI;

import it.polimi.ingsw.exceptions.ClientSideCheckException;
import it.polimi.ingsw.messages.lobby.client.lobbysetup.RequestLobbyNamesListMessage;
import it.polimi.ingsw.messages.lobby.server.ServerLobbyMessage;
import it.polimi.ingsw.mvc.model.Model;
import it.polimi.ingsw.mvc.view.CLIStringHandler.LobbyCLIStringHandler.CLILobbyNameHandler;
import it.polimi.ingsw.mvc.view.CLIStringHandler.LobbyCLIStringHandler.LobbyCLIStringHandler;
import it.polimi.ingsw.mvc.view.lobby.LobbyView;
import it.polimi.ingsw.notifier.Notifier;

import java.util.Scanner;

/**
 * CLI client View for the lobby
 */
public class CLILobbyView extends LobbyView {
    private String frontEnd;
    private String currentQueryMessage;
    private LobbyCLIStringHandler cliStringHandler;

    private Model firstModel;

    private Thread readInputThread;

    // True if we created a new lobby, or at least we tried
    private boolean isFirstPlayer;

    public CLILobbyView(Notifier<ServerLobbyMessage> modelNotifier) {
        super(modelNotifier);
    }

    public void show() {
        System.out.println(frontEnd);
        System.out.println(currentQueryMessage);
    }

    public void setLobbyReloadMessagesAndHandler() {
        frontEnd = "Loading lobbies...";
        currentQueryMessage = "";
        cliStringHandler = new CLILobbyNameHandler();

        show();
    }

    @Override
    public void run() throws InterruptedException {
        // Query lobbies from server, and set initial message
        setLobbyReloadMessagesAndHandler();

        notifySubscribers(new RequestLobbyNamesListMessage());

        asyncReadStdin();
        readInputThread.join();
    }

    public void stop() {
        readInputThread.interrupt();
    }

    @Override
    public void subscribeNotification(ServerLobbyMessage newMessage) {
        newMessage.updateCLI(this);

        show();
    }

    private void asyncReadStdin() {
        readInputThread = new Thread(() -> {
            final Scanner stdin = new Scanner(System.in);

            while (true) {
                String newLine = stdin.nextLine().trim().replaceAll(" +", " ");

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

    public void setCliStringHandler(LobbyCLIStringHandler cliStringHandler) {
        this.cliStringHandler = cliStringHandler;
    }

    public boolean isFirstPlayer() {
        return isFirstPlayer;
    }

    public void setFirstPlayer(boolean firstPlayer) {
        isFirstPlayer = firstPlayer;
    }

    public void setFirstModel(Model firstModel) {
        this.firstModel = firstModel;
    }

    @Override
    public Model getFirstModel() {
        return firstModel;
    }
}
