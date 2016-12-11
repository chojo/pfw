package client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Collections;

import processing.core.PApplet;
import processing.core.PVector;
import processing.core.PFont;
import processing.event.KeyEvent;

import shared.Snake;
import shared.Food;
import shared.GameSocket;
import shared.Connection;
import shared.MessageHandler;

public class SnakeTest extends PApplet {

    public static final int SCREEN_X = server.Server.FIELD_X;
    public static final int SCREEN_Y = server.Server.FIELD_Y;
    public static final int MAX_FOOD = 30;
    public static PFont f;

    List<Food> foods = Collections.synchronizedList(new LinkedList<>());

    Connection connection;

    static final Map<String, Snake> snakes = new HashMap<>();
    static final Random random = new Random();

    String playerName;

    public static void main(String[] args)  {
        PApplet.main("client.SnakeTest");
    }


    @Override
    public void settings() {
        size(SCREEN_X,SCREEN_Y, "processing.opengl.PGraphics2D");
    }


    public void drawSnake(Snake snake) {
        for (int i = snake.getParts().size()-1; i>=0; i--) {
            PVector v = snake.getParts().get(i);
            ellipse(v.x, v.y, 20,20);
        }
        fill(0);
        PVector textPosition = PVector.div(PVector.add(snake.head(), snake.tail()), 2);
        text(snake.getName(), textPosition.x, textPosition.y);
        fill(255);
    }

    @Override
    public void setup() {
        f = createFont("Arial",16,true);

        playerName = "AnonymousSnake" + Integer.toString(random.nextInt(100));
        snakes.put(playerName, new Snake(playerName));
        try {
            connection = new ServerConnection(
                    new ClientGameSocket(this, "127.0.0.1", 3000), this);
        } catch(UnknownHostException e) {
            // FIXME This needs error handling.
            e.printStackTrace();
        } catch(IOException e) {
            // FIXME This needs error handling.
            e.printStackTrace();
        }
        connection.putMessageHandler("pos", new PosMessageHandler());
        connection.putMessageHandler("dir", new DirMessageHandler());
        connection.putMessageHandler("score", new ScoreMessageHandler());
        connection.putMessageHandler("die", new DieMessageHandler());
        connection.putMessageHandler("eat", new EatMessageHandler());
        connection.putMessageHandler("feed", new FeedMessageHandler());
        connection.start();
    }

    @Override
    public void draw() {
        background(255);
        rect(0, 0, SCREEN_X-1, SCREEN_Y-1);
        textAlign(CENTER, CENTER);

        synchronized (snakes) {
            for (Snake snake : snakes.values()) {
                snake.moveBy(1 / frameRate);
                drawSnake(snake);
            }
        }

        if (getSnake().isTurning()) {
            connection.send("dir "
                    + getSnake().getDirection().x + " "
                    + getSnake().getDirection().y);
        }

        synchronized (foods) {
            for (Food food : foods) {
                ellipse(food.x, food.y, Food.SIZE, Food.SIZE);
            }
        }
    }

    public void drawGameOver() {
        background(255);
        textFont(f,16);
        fill(0);
        text("Game Over!",10,100);
    }

    private void gameOver() {
        // TODO Game over logic.
        drawGameOver();
        exit();
    }

    @Override
    public void keyPressed(KeyEvent event) {
        switch (event.getKeyCode()) {
            case 37:
                getSnake().goLeft();
                break;
            case 39:
                getSnake().goRight();
                break;
        }
    }

    @Override
    public void keyReleased() {
        getSnake().goStraight();
    }

    public class ClientGameSocket extends GameSocket {
        ClientGameSocket(PApplet pApplet, String host, int port)
                throws UnknownHostException, IOException {
            super(new Socket(host, port));
            pApplet.registerMethod("dispose", this);
        }

        public void dispose() throws IOException {
            this.getSocket().close();
        }
    }

    public String getPlayerName() {
        return playerName;
    }

    public Snake getSnake() {
        return getSnake(playerName);
    }

    public Snake getSnake(String name) {
        return snakes.get(name);
    }

    public Snake putSnake(String name, Snake snake) {
        return snakes.put(name, snake);
    }

    public class PosMessageHandler implements MessageHandler {
        @Override
        public void handle(Scanner scanner, Connection connection) {
            String name = scanner.next();
            final Snake snake = getSnake(name);
            if (snake == null) {
                putSnake(
                        name,
                        new Snake(
                            name,
                            scanner.nextFloat(),
                            scanner.nextFloat(),
                            new PVector(1, 0)));
            } else {
                snake.moveTo(
                        new PVector(scanner.nextFloat(), scanner.nextFloat()));
            }
        }
    }

    public class DirMessageHandler implements MessageHandler {
        @Override
        public void handle(Scanner scanner, Connection connection) {
            String name = scanner.next();
            if (playerName.equals(name)) { return; }
            getSnake(name)
                .setDirection(
                        new PVector(scanner.nextFloat(), scanner.nextFloat()));
        }
    }

    public class ScoreMessageHandler implements MessageHandler {
        @Override
        public void handle(Scanner scanner, Connection connection) {
            getSnake(scanner.next()).grow(scanner.nextInt() - Snake.SIZE);
        }
    }

    public class DieMessageHandler implements MessageHandler {
        @Override
        public void handle(Scanner scanner, Connection connection) {
            snakes.remove(scanner.next());
            if (getSnake() == null) { gameOver(); }
        }
    }

    public class EatMessageHandler implements MessageHandler {
        @Override
        public void handle(Scanner scanner, Connection connection) {
            getSnake(scanner.next()).grow(Food.GROWTH_FACTOR);
            foods.remove(scanner.nextInt());
        }
    }

    public class FeedMessageHandler implements MessageHandler {
        @Override
        public void handle(Scanner scanner, Connection connection) {
            foods.add(new Food(scanner.nextFloat(), scanner.nextFloat()));
        }
    }
}
