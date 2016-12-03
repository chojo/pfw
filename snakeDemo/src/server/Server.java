package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static Game game = new Game();

    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(3000);
        game.start();

        while (true) {
            Socket client = server.accept();
            System.out.println("Client connected");
            PlayerConnection playerConnection = new PlayerConnection(client, game);
            game.registerClient(playerConnection);
            playerConnection.start();
        }
    }
}
