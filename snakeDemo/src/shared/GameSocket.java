package shared;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

public class GameSocket {
    final Socket socket;
    final PrintWriter output;
    final BufferedReader input;

    public GameSocket(Socket socket) throws IOException {
        this.socket = socket;
        output = new PrintWriter(socket.getOutputStream(), true);
        input = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));
    }

    public Socket getSocket() {
        return socket;
    }

    public PrintWriter getOutput() {
        return output;
    }

    public BufferedReader getInput() {
        return input;
    }
}
