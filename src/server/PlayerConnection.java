package server;

import java.io.IOException;

import shared.Connection;
import shared.GameSocket;

public class PlayerConnection extends Connection {
    private final Game game;

    public PlayerConnection(GameSocket gameSocket, Game game) throws IOException {
        // Expect player name as first input
        super(gameSocket, gameSocket.getInput().readLine());

        this.game = game;
    }

    @Override
    public void run() {
        super.run();
        game.unregisterClient(this);
    }
}
