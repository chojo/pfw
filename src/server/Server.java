package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Scanner;

import shared.GameSocket;
import shared.Connection;

public class Server {
    public static final int FIELD_X = 424;
    public static final int FIELD_Y = 768;

    private static Game game = new Game();

    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(4000);
        game.start();

        while (true) {
            GameSocket client = new GameSocket(server.accept());
            System.out.println("Client connected");
            Connection connection = new PlayerConnection(client, game);
            game.registerClient(connection);
            connection.start();
        }
    }
}
