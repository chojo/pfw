package client;

import java.util.LinkedList;
import java.util.List;

import processing.core.PApplet;
import processing.core.PVector;
import processing.event.KeyEvent;
import shared.Snake;
import shared.Food;

enum Rotation {
    NONE, LEFT, RIGHT
}

public class SnakeTest extends PApplet {

    public static final int SCREEN_X = 1024;
    public static final int SCREEN_Y = 768;
    public static final int MAX_FOOD = 30;
    public static final int GROWING_FACTOR = 2;
    
    List<Food> foodlist = new LinkedList<>();

    static PVector direction = new PVector(1,0);
    static Rotation rotation = Rotation.NONE;
    static Snake snake = new Snake(100,100);


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
            		snake.grow(GROWING_FACTOR);
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
    	return (checkFoodProximity(snake.head().x, currentFood.getX()) && checkFoodProximity(snake.head().y, currentFood.getY()));
    }
    

    @Override
    public void draw() {    	
        background(255);

        if (rotation == Rotation.LEFT) {
            direction.rotate(-0.1f);
        } else if (rotation == Rotation.RIGHT) {
            direction.rotate(0.1f);
        }

        snake.moveBy(direction);
        drawSnake(snake);
        
        

        
        
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
}
