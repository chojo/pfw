package client;

import java.io.IOException;
import java.util.Locale;
import java.util.Scanner;

import processing.core.PVector;

import shared.GameSocket;
import shared.Connection;
import shared.Snake;

import client.SnakeTest;

public class ServerConnection extends Connection {
    private final SnakeTest snakeTest;

    public ServerConnection(GameSocket gameSocket, SnakeTest snakeTest) 
            throws IOException {
        super(gameSocket, snakeTest.getPlayerName());
        this.snakeTest = snakeTest;
        send(getPlayerName());
    }
}
