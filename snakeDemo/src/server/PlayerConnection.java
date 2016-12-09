package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Locale;
import java.util.Scanner;

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
