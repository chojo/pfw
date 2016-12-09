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

    @Override
    public void handle(String msg) {
        if (msg.startsWith("pos")) {
            Scanner scanner =
                new Scanner(msg.substring(3)).useLocale(Locale.US);
            String name = scanner.next();
            final Snake snake = snakeTest.getSnake(name);
            if (snake == null) {
                snakeTest.putSnake(
                        name, 
                        new Snake(scanner.nextFloat(), scanner.nextFloat()));
            } else {
                snake.moveTo(
                        new PVector(scanner.nextFloat(), scanner.nextFloat()));
            }
        }
    }
}
