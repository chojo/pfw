package client;

import java.io.IOException;

import shared.GameSocket;
import shared.Connection;

public class ServerConnection extends Connection {
    private final SnakeTest snakeTest;

    public ServerConnection(GameSocket gameSocket, SnakeTest snakeTest) 
            throws IOException {
        super(gameSocket, snakeTest.getPlayerName());
        this.snakeTest = snakeTest;
        send(getPlayerName());
    }
}
