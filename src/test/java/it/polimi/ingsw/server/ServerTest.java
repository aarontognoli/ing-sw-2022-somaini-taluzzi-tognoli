package it.polimi.ingsw.server;

import it.polimi.ingsw.client.Client;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class ServerTest {

    private Server runServerAsync() {
        Server server;
        try {
            server = new Server();
        } catch (IOException e) {
            e.printStackTrace();
            assert false;
            return null;
        }

        Thread t = new Thread(server::runServer);
        t.start();

        return server;
    }

    @Test
    void connection() {
        Server server = runServerAsync();

        assertNotNull(server);

        server.closePlayersConnections();

        Client c1 = new Client("localhost", Server.PORT);
        // c1.run();
    }
}