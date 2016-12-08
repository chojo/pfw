package client;

import processing.core.PApplet;
import processing.core.PVector;
import processing.event.KeyEvent;
import shared.Snake;
import shared.Food;

enum Rotation {
    NONE, LEFT, RIGHT
}

public class SnakeTest extends PApplet {

    public static final int SCREEN_X = 600;
    public static final int SCREEN_Y = 400;


    static PVector direction = new PVector(1,0);
    static Rotation rotation = Rotation.NONE;
    static Snake snake = new Snake(100,100);
    private Food food = null; //FOOD

    private PVector v = new PVector(100,100);


    public static void main(String[] args)  {
        PApplet.main("client.SnakeTest");
    }


    @Override
    public void settings() {
        size(SCREEN_X,SCREEN_Y, "processing.opengl.PGraphics2D");
    }


    public void drawSnake(Snake snake) {
        for (int i = snake.getParts().size()-1; i>=0; i--) {
            v = snake.getParts().get(i);
            ellipse(v.x, v.y, 20,20);
            PVector buffer = new PVector (v.x,v.y);
            v = buffer;
        }
        println(v.x);
    }

    public int getScreenX() {
        return SCREEN_X;
    }

    public int getScreenY() {
        return SCREEN_Y;
    }
    
    //FOOD
    public void setFood() { 
        this.food = new Food(SCREEN_X, SCREEN_Y);
    }
    
    public void drawFood() {
    	if (this.food != null) {
    		ellipse(this.food.getX(), this.food.getY(), 10,10);
    	}
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
    public Boolean isEaten() {
    	return (checkFoodProximity(snake.head().x, food.getX()) && checkFoodProximity(snake.head().y, food.getY()));
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

        if ((v.x  + 10) > SCREEN_X || (v.x  + 10) < 0 || (v.y  + 10) > SCREEN_Y ||(v.y  + 10) < 0)
            System.out.println("GAME OVER");

        drawSnake(snake);
        
        //FOOD
        if (food == null) {
        	setFood();
        } else {
        	if (isEaten()) {
        		setFood();
        	} else {
        	drawFood();
        	}
        }
    }

    @Override
    public void keyPressed(KeyEvent event) {
        System.out.println(event.getKeyCode());

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
