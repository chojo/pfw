package server;

import java.util.HashMap;
import java.util.Map;

import processing.core.PVector;

import shared.Connection;
import shared.Snake;

public class Game extends Thread{

    class Player {
        final Connection connection;
        final Snake snake;
        final PVector direction;

        public Player(Connection connection, Snake snake, PVector direction) {
            this.connection = connection;
            this.snake = snake;
            this.direction = direction;
        }

        public void move() {
            snake.moveBy(direction);
        }

        public String position() {
            return "pos "+ connection.getPlayerName()+" "+snake.head().x+" "+snake.head().y;
        }

        public String death() {
            return "die " + connection.getPlayerName();
        }

        public boolean borderCollision() {
            // FIXME NYI
            return false;
        }

    }

    private final Map<String, Player> players = new HashMap<>();

    public synchronized void registerClient(Connection connection) {
        System.out.println("Client registered: "+ connection.getPlayerName());
        players.put(
                connection.getPlayerName(),
                new Player(connection, new Snake(), new PVector(1,0))
        );
    }

    public synchronized void unregisterClient(Connection connection) {
        System.out.println("Client unregistered: "+ connection.getPlayerName());
        broadcast(players.get(connection.getPlayerName()).death());
        connection.close();
        players.remove(connection.getPlayerName());
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            for (Player player : players.values()) {
                player.move();
            }

            for (Player player : players.values()) {
                broadcast(player.position());
            }
        }
    }

    private void broadcast(String message) {
        for (Player p : players.values()) {
            p.connection.send(message);
        }
    }

    public synchronized void setDirection(String playerName, float x, float y) {
        players.get(playerName).direction.set(x,y).normalize();
    }
}
