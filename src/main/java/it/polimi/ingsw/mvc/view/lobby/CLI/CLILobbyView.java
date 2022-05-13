package it.polimi.ingsw.mvc.view.lobby.CLI;

import it.polimi.ingsw.messages.lobby.client.lobbysetup.RequestLobbyNamesListMessage;
import it.polimi.ingsw.messages.lobby.server.ServerLobbyMessage;
import it.polimi.ingsw.mvc.view.lobby.CLI.CLIStringHandler.BaseCLIStringHandler;
import it.polimi.ingsw.mvc.view.lobby.CLI.CLIStringHandler.CLILobbyNameHandler;
import it.polimi.ingsw.mvc.view.lobby.LobbyView;
import it.polimi.ingsw.notifier.Notifier;

import java.util.Scanner;

/**
 * CLI client View for the lobby
 */
public class CLILobbyView extends LobbyView {

    private String frontEnd;
    private String currentQueryMessage;
    private Thread readInputThread;

    private BaseCLIStringHandler cliStringHandler;

    public CLILobbyView(Notifier<ServerLobbyMessage> modelNotifier) {
        super(modelNotifier);
    }

    public void show() {
        System.out.println(frontEnd);
        System.out.println(currentQueryMessage);
    }

    @Override
    public void run() {
        // Query lobbies from server, and set initial message
        frontEnd = "Loading lobbies...";
        currentQueryMessage = "";
        cliStringHandler = new CLILobbyNameHandler();
        show();

        notifySubscribers(new RequestLobbyNamesListMessage());

        asyncReadStdin();
    }

    public void stop() {
        readInputThread.interrupt();
    }

    @Override
    public void subscribeNotification(ServerLobbyMessage newMessage) {
        CLILobbyViewUpdate update = newMessage.getUpdateForCLI();

        frontEnd = update.newFrontEnd();
        currentQueryMessage = update.newCurrentQueryMessage();
        cliStringHandler = update.newCliStringHandler();

        show();
    }

    private void asyncReadStdin() {
        readInputThread = new Thread(() -> {
            final Scanner stdin = new Scanner(System.in);

            while (true) {
                String newLine = stdin.nextLine();

                notifySubscribers(cliStringHandler.generateMessageFromInput(newLine));
            }
        });

        readInputThread.start();
    }
}
