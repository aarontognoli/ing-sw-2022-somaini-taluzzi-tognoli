package it.polimi.ingsw.server;

import it.polimi.ingsw.enums.DeckName;
import it.polimi.ingsw.enums.GameMode;
import it.polimi.ingsw.mvc.view.RemoteView;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.Remote;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class SocketClientConnection implements ClientConnection, Runnable {

    private final Socket socket;
    private ObjectOutputStream out;
    private final Server server;
    private RemoteView remoteView;

    private boolean active = true;

    public SocketClientConnection(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
    }

    public void setRemoteView(RemoteView remoteView) {
        this.remoteView = remoteView;
    }

    public void redirectToRemoteView(Object o) {
        remoteView.receiveClientCommunication(o);
    }


    private synchronized boolean isActive(){
        return active;
    }

    private synchronized void send(Object message) {
        try {
            out.reset();
            out.writeObject(message);
            out.flush();
        } catch(IOException e){
            System.err.println(e.getMessage());
        }

    }

    public synchronized void closeConnection() {
        send("Connection closed!");
        try {
            socket.close();
        } catch (IOException e) {
            System.err.println("Error when closing socket!");
        }
        active = false;
    }

    private void close() {
        closeConnection();
        System.out.println("A player disconnected... End of the game");
        server.closePlayersConnections();
        System.out.println("Game ended");
    }

    public void asyncSend(Object message){
        new Thread(() -> send(message)).start();
    }

    @Override
    public void run() {
        Scanner in; //valid for the CLI, TODO GUI...
        String read;
        String name;
        DeckName deckName = null;
        GameMode gameMode = null;
        boolean valid = true;
        boolean firstPlayer = false;
        try{
            in = new Scanner(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());

            send("Welcome!\nWhat is your name?");
            do {
                name = in.nextLine();
                valid = true;
                synchronized (server.nicknamesAndDecks) {
                    for (String n : server.nicknamesAndDecks.keySet()) {
                        if (n.equals(name)) {
                            send("This nickname was already chosen. Choose another one!");
                            valid = false;
                            break;
                        }
                    }
                    // First player (who gets to choose game mode and number of players)
                    // is the first who inserts his nickname
                    if (valid) {
                        server.nicknamesAndDecks.put(name, null);
                        if (server.nicknamesAndDecks.size() == 1) {
                            firstPlayer = true;
                        }
                    }
                }
            }while(!valid);

            send("Choose a Deck Name from the following: DESERT_KING, MOUNTAIN_SAGE, " +
                    "CLOUD_WITCH, FOREST_MAGE.");
            do {
                read = in.nextLine();
                valid = true;
                try {
                    deckName = DeckName.valueOf(read);
                } catch (IllegalArgumentException e) {
                    send("Deck Name not valid. Try again!");
                    valid = false;
                }
                if (valid) {
                    synchronized (server.nicknamesAndDecks) {
                        for (DeckName d : server.nicknamesAndDecks.values()) {
                            if (d.equals(deckName)) {
                                send("This nickname was already chosen. Choose another one!");
                                valid = false;
                                break;
                            }
                        }
                        if (valid) {
                            server.nicknamesAndDecks.replace(name, deckName);
                        }
                    }
                }
            }while(!valid);

            if (firstPlayer) {
                send("Select the number of players: 2, 3 or 4");
                int num = 0;
                do {
                    read = in.nextLine();
                    valid = true;
                    try {
                        num = Integer.parseInt(read);
                    } catch (NumberFormatException e) {
                        send("Please select a valid number!");
                        valid = false;
                    }
                    if (valid) {
                        if (num < 2 || num > 4) {
                            send("Please select a valid number!");
                            valid = false;
                        }
                    }
                }while(!valid);
                server.numberOfPlayers = num;

                send("Choose a Game Mode from the following: EASY_MODE, EXPERT_MODE");
                do {
                    read = in.nextLine();
                    valid = true;
                    try {
                        gameMode = GameMode.valueOf(read);
                    } catch (IllegalArgumentException e) {
                        send("Game Mode not valid. Try again!");
                        valid = false;
                    }
                }while(!valid);
                server.gameMode = gameMode;

                send("Select a number from 1 to 12, Mother Nature will be positioned" +
                        "in the correspondent island!");
                do {
                    read = in.nextLine();
                    valid = true;
                    try {
                        num = Integer.parseInt(read);
                    } catch (NumberFormatException e) {
                        send("Please select a valid number!");
                        valid = false;
                    }
                    if (valid) {
                        if (num < 1 || num > 12) {
                            send("Please select a valid number!");
                            valid = false;
                        }
                    }
                }while(!valid);
                server.motherNatureStartingPosition = num;
            }

            server.lobby(this, name);

            while(isActive()){
                read = in.nextLine();
                redirectToRemoteView(read);
            }

        } catch (IOException | NoSuchElementException e) {
            System.err.println("Error!" + e.getMessage());
        } finally {
            close();
        }
    }
}
