package it.polimi.ingsw;

import it.polimi.ingsw.client.Client;

import java.io.IOException;

public class ClientApp {
    public static void main(String[] args) {
        boolean isCli = !(args.length >= 1 && (args[0].equals("gui") || args[0].equals("GUI")));

        Client client = new Client("127.0.0.1", 12345, isCli);
        try {
            client.run();
        } catch (IOException | InterruptedException e) {
            System.err.println(e.getMessage());
        }
    }


}
