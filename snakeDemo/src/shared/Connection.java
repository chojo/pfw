package shared;

import java.io.IOException;

import shared.GameSocket;

public abstract class Connection extends Thread {
    private final String playerName;
    private final GameSocket gameSocket;

    public Connection(GameSocket gameSocket, String playerName) 
            throws IOException {
        this.gameSocket = gameSocket;
        this.playerName = playerName;
    }

    @Override
    public void run() {
        String line;
        try {
            while ((line = receive()) != null) {
                handle(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(String msg) {
        gameSocket.getOutput().println(msg);
        gameSocket.getOutput().flush();
    }

    public String receive() throws IOException {
        return gameSocket.getInput().readLine();
    }

    public String getPlayerName() {
        return playerName;
    }

    public abstract void handle(String msg);
}
