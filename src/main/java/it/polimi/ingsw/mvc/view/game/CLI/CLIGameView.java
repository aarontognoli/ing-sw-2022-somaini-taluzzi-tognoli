package it.polimi.ingsw.mvc.view.game.CLI;

import it.polimi.ingsw.exceptions.ClientSideCheckException;
import it.polimi.ingsw.mvc.model.Model;
import it.polimi.ingsw.mvc.view.CLIStringHandler.GameCLIStringHandler.CLIPlayAssistantHandler;
import it.polimi.ingsw.mvc.view.CLIStringHandler.GameCLIStringHandler.GameCLIStringHandler;
import it.polimi.ingsw.mvc.view.game.ClientGameView;
import it.polimi.ingsw.notifier.Notifier;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CLIGameView extends ClientGameView {
    private String frontEnd;
    private String currentQueryMessage;
    private GameCLIStringHandler cliStringHandler;

    private Thread readInputThread;

    public CLIGameView(Notifier<Model> modelNotifier) {
        super(modelNotifier);
    }

    public void show() {
        System.out.println(frontEnd);
        System.out.println(currentQueryMessage);
    }

    public void showModel() {
        // TODO: Draw on screen the updated model
        System.out.println(model);
    }

    public void run() {
        frontEnd = "Loading first model...";
        currentQueryMessage = "";
        show();
        showModel();

        frontEnd = "";
        currentQueryMessage = "Choose an assistant card typing the number you see on its upper left";
        cliStringHandler = new CLIPlayAssistantHandler();
        show();

        asyncReadStdin();
        try {
            readInputThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        readInputThread.interrupt();
    }

    @Override
    public void subscribeNotification(Model newValue) {
        super.subscribeNotification(newValue);
        // TODO if it's my turn I print the model and the new query message
        // if(newValue.publicModel.getCurrentPlayer().getNickname().equals(...))
        // {show()}
    }

    private void asyncReadStdin() {
        readInputThread = new Thread(() -> {
            final BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));

            while (true) {
                String newLine;
                try {
                    // We cannot directly do stdin.readLine() because it is not interruptible
                    // therefore the solution is to continuosly poll stdin while explicitly
                    // put the thread in a WAIT state, which is interruptible.
                    while (!stdin.ready()) {
                        Thread.sleep(200);
                    }
                    newLine = stdin.readLine().trim().replaceAll(" +", " ");
                } catch (InterruptedException e) {
                    // Interrupt received while polling input, stop() was called.
                    break;
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

    public void setCliStringHandler(GameCLIStringHandler cliStringHandler) {
        this.cliStringHandler = cliStringHandler;
    }

}
