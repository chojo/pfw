package shared;

import java.util.Scanner;

@FunctionalInterface
public interface MessageHandler {
    public void handle(Scanner scanner, Connection connection);
}

