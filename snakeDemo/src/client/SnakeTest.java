package client;

import processing.core.PApplet;
import processing.core.PVector;
import processing.event.KeyEvent;
import shared.Snake;

enum Rotation {
    NONE, LEFT, RIGHT
}

public class SnakeTest extends PApplet {

    public static final int SCREEN_X = 1024;
    public static final int SCREEN_Y = 768;


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
