package it.polimi.ingsw;

import it.polimi.ingsw.client.Client;

import java.io.IOException;

public class ClientApp {

    private static void printUsage() {
        System.out.println("Invalid arguments. Usage:");
        System.out.println("<gui | cli> <server_ip>");
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            printUsage();
            return;
        }

        boolean isCli;

        String interfaceMode = args[0].toLowerCase();
        String ip = args[1];

        if (interfaceMode.equals("cli")) {
            isCli = true;
        } else if (interfaceMode.equals("gui")) {
            isCli = false;
        } else {
            printUsage();
            return;
        }

        Client client = new Client(ip, 12345, isCli);
        try {
            client.run();
        } catch (IOException | InterruptedException e) {
            System.err.println(e.getMessage());
        }
    }
}
