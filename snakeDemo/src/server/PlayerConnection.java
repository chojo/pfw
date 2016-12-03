package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Locale;
import java.util.Scanner;

public class PlayerConnection extends Thread {
    private final Game game;
    private final BufferedReader input;
    private final PrintWriter output;
    private final String playerName;

    public PlayerConnection(Socket socket, Game game) throws IOException {
        this.input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.output = new PrintWriter(socket.getOutputStream());
        this.game = game;

        // Expect player name as first input
        this.playerName = input.readLine();
    }

    @Override
    public void run() {
        String line;
        try {
            while ( (line = input.readLine()) != null) {
                if (line.startsWith("dir")) {
                    Scanner s = new Scanner(line.substring(3)).useLocale(Locale.US);
                    game.setDirection(playerName, s.nextFloat(), s.nextFloat());
                }
            }
            game.unregisterClient(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void send(String msg) {
        output.println(msg);
        output.flush();
    }

    public String getPlayerName() {
        return playerName;
    }
}
