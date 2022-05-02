package it.polimi.ingsw.server;

import it.polimi.ingsw.enums.DeckName;
import it.polimi.ingsw.enums.GameMode;
import it.polimi.ingsw.exceptions.ObjectIsNotMessageException;
import it.polimi.ingsw.messages.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class SocketClientConnection implements ClientConnection, Runnable {

    private final Socket socket;
    private ObjectOutputStream out;
    private final Server server;

    private boolean active = true;

    public SocketClientConnection(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
    }

    public void redirectToRemoteView(Object message) throws ObjectIsNotMessageException {
        if (!(message instanceof Message)) {
            throw new ObjectIsNotMessageException();
        } else {
            ((Message) message).getRemoteView().redirectMessageToController((Message) message);
        }
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
        Scanner in;
        String read;
        int numRead = 0;
        String name;
        DeckName deckName;
        boolean valid;
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

            send("Choose a Deck Name from the following: DESERT_KING, MOUNTAIN_SAGE, CLOUD_WITCH, " +
                    "FOREST_MAGE. Type 1 for DESERT_KING, 2 for MOUNTAIN_SAGE, 3 for CLOUD_WITCH, " +
                    "4 for FOREST_MAGE");
            do {
                read = in.nextLine();
                valid = true;
                try {
                    numRead = Integer.parseInt(read);
                } catch (NumberFormatException e) {
                    send("Please select a valid number!");
                    valid = false;
                }
                if (valid) {
                    if (numRead < 1 || numRead > 4) {
                        send("Please select a valid number!");
                        valid = false;
                    } else {
                        deckName = DeckName.values()[numRead - 1];
                        synchronized (server.nicknamesAndDecks) {
                            for (DeckName d : server.nicknamesAndDecks.values()) {
                                if (d.equals(deckName)) {
                                    send("This deck name was already chosen. Choose another one!");
                                    valid = false;
                                    break;
                                }
                            }
                            if (valid) {
                                server.nicknamesAndDecks.replace(name, deckName);
                            }
                        }
                    }
                }
            }while(!valid);

            if (firstPlayer) {
                send("Select the number of players: 2, 3 or 4");
                do {
                    read = in.nextLine();
                    valid = true;
                    try {
                        numRead = Integer.parseInt(read);
                    } catch (NumberFormatException e) {
                        send("Please select a valid number!");
                        valid = false;
                    }
                    if (valid) {
                        if (numRead < 2 || numRead > 4) {
                            send("Please select a valid number!");
                            valid = false;
                        }
                    }
                }while(!valid);
                server.numberOfPlayers = numRead;

                send("Choose a Game Mode from the following: EASY_MODE, EXPERT_MODE. " +
                        "Type 1 for EASY_MODE, 2 for EXPERT_MODE.");
                do {
                    read = in.nextLine();
                    valid = true;
                    try {
                        numRead = Integer.parseInt(read);
                    } catch (NumberFormatException e) {
                        send("Please select a valid number!");
                        valid = false;
                    }
                    if (valid) {
                        if (numRead < 1 || numRead > 2) {
                            send("Please select a valid number!");
                            valid = false;
                        }
                    }
                }while(!valid);
                server.gameMode = GameMode.values()[numRead - 1];

                send("Select a number from 1 to 12, Mother Nature will be positioned" +
                        "in the correspondent island!");
                do {
                    read = in.nextLine();
                    valid = true;
                    try {
                        numRead = Integer.parseInt(read);
                    } catch (NumberFormatException e) {
                        send("Please select a valid number!");
                        valid = false;
                    }
                    if (valid) {
                        if (numRead < 1 || numRead > 12) {
                            send("Please select a valid number!");
                            valid = false;
                        }
                    }
                }while(!valid);
                server.motherNatureStartingPosition = numRead;
            }

            server.lobby(this, name);

            //now the player should only send objects of class Message
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            Object o;
            while(isActive()){
                o = inputStream.readObject();
                redirectToRemoteView(o);
            }

        } catch (IOException | NoSuchElementException | ClassNotFoundException | ObjectIsNotMessageException e) {
            System.err.println("Error!" + e.getMessage());
        } finally {
            close();
        }
    }
}
