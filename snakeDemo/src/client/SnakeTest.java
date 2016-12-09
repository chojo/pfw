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

import processing.core.PApplet;
import processing.core.PVector;
import processing.event.KeyEvent;

import shared.Snake;
import shared.Food;
import shared.GameSocket;
import shared.Connection;
import shared.MessageHandler;

enum Rotation {
    NONE, LEFT, RIGHT
}

public class SnakeTest extends PApplet {

    public static final int SCREEN_X = 1024;
    public static final int SCREEN_Y = 768;
    public static final int MAX_FOOD = 30;
    public static final int GROWING_FACTOR = 2;
    
    List<Food> foodlist = new LinkedList<>();

    Connection connection;

    static PVector direction = new PVector(1,0);
    static Rotation rotation = Rotation.NONE;
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
    }

    public int getScreenX() {
        return SCREEN_X;
    }

    public int getScreenY() {
        return SCREEN_Y;
    }
    
    //FOOD
    public void setFood() { 
        Food newFood = new Food(SCREEN_X, SCREEN_Y);
        this.foodlist.add(newFood);
        //System.out.println(newFood.getId());
    }
    
    //FOOD
    public void drawFoodList() {
    	if (this.foodlist != null) {
    		for (int i = 0; i < foodlist.size(); i++) {
    			Food currentFood = this.foodlist.get(i);
    			if (!isEaten(currentFood)) {
    				ellipse(currentFood.getX(), currentFood.getY(), 10,10);
    			} else {
    				//System.out.println("No " + currentFood.getId() + " is eaten!");
    				this.foodlist.remove(i);
            		getSnake().grow(GROWING_FACTOR);
    			}
    		}
    	}
    	//System.out.println("Groesse der foodlist: " + this.foodlist.size());
    }
    
    //FOOD
    // checks if the snake is close enough to eat the food
    public Boolean checkFoodProximity(float snakeInt, float foodInt) {
    	int s = (int)snakeInt;
    	int f = (int)foodInt;
    	int closerThan = 10;
    	
    	return (Math.abs((long)(s - f)) <= closerThan);
    }
    
    //FOOD
    public Boolean isEaten(Food currentFood) {
    	return (checkFoodProximity(getSnake().head().x, currentFood.getX()) && checkFoodProximity(getSnake().head().y, currentFood.getY()));
    }
    
    @Override
    public void setup() {
        playerName = "AnonymousSnake" + Integer.toString(random.nextInt(100));
        snakes.put(playerName, new Snake());
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
        connection.putMessageHandler("die", new DieMessageHandler());
        connection.start();
    }

    @Override
    public void draw() {    	
        background(255);

        if (rotation == Rotation.LEFT) {
            direction.rotate(-0.1f);
        } else if (rotation == Rotation.RIGHT) {
            direction.rotate(0.1f);
        }

        if (rotation != Rotation.NONE) {
            connection.send("dir "
                    + playerName + " "
                    + direction.x + " "
                    + direction.y);
        }

        getSnake().moveBy(PVector.div(direction, frameRate));
        drawSnake(getSnake());
        
        

        
        
        // FOOD
        if (this.foodlist.isEmpty()) {
        	setFood();
        }
        
        if (Math.random() < 0.005) {
        	if (this.foodlist.size() < MAX_FOOD) {
        		setFood();
        	}
        	
        }
        drawFoodList();

        
        
    }

    private void gameOver() {
        // TODO Game over logic.
        exit();
    }

    @Override
    public void keyPressed(KeyEvent event) {
        //System.out.println(event.getKeyCode());

        switch (event.getKeyCode()) {
            case 37:
                rotation = Rotation.LEFT;
                break;
            case 39:
                rotation = Rotation.RIGHT;
                break;
        }
    }

    @Override
    public void keyReleased() {
        rotation = Rotation.NONE;
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
        public void handle(Scanner scanner) {
            String name = scanner.next();
            final Snake snake = getSnake(name);
            if (snake == null) {
                putSnake(
                        name, 
                        new Snake(scanner.nextFloat(), scanner.nextFloat()));
            } else {
                snake.moveTo(
                        new PVector(scanner.nextFloat(), scanner.nextFloat()));
            }
        }
    }

    public class DieMessageHandler implements MessageHandler {
        @Override
        public void handle(Scanner scanner) {
            snakes.remove(scanner.next());
            if (getSnake() == null) { gameOver(); }
        }
    }
}
