package shared;

import processing.core.PVector;

import java.util.LinkedList;
import java.util.List;

enum Rotation {
    NONE, LEFT, RIGHT
}

public class Snake {
    public static final int SIZE = 20;
    public static final int SPEED = 50;

    final String name;

    private PVector direction;
    private Rotation rotation = Rotation.NONE;
    private List<PVector> parts = new LinkedList<>();

    public Snake(String name) {
        this(name, 100, 100, new PVector(1, 0));
    }

    public Snake(String name, float x, float y, PVector direction) {
        for (int i = 0; i < SIZE; i++) {
            parts.add(new PVector(x,y));
        }
        this.direction = direction;
        this.name = name;
    }

    public void moveTo(PVector newHead) {
        if (rotation == Rotation.LEFT) {
            direction.rotate(-0.1f);
        } else if (rotation == Rotation.RIGHT) {
            direction.rotate(0.1f);
        }

        List<PVector> newparts = new LinkedList<>();
        newparts.add(newHead.copy());

        for (int i = 1; i< parts.size(); i++) {
            PVector head = parts.get(i-1);
            PVector tail = parts.get(i);

            PVector diff = PVector.sub(head, tail);
            diff.mult(0.1f);
            newparts.add(PVector.add(tail, diff));
        }

        parts = newparts;
    }

    public void moveBy(Float distance) {
        moveTo(PVector.add(head(), PVector.mult(direction, SPEED * distance)));
    }

    public PVector head() {
        return parts.get(0);
    }

    public PVector tail() {
        return parts.get(parts.size() - 1);
    }

    public String getName() {
        return name;
    }

    public PVector getDirection() {
        return direction;
    }

    public PVector setDirection(PVector direction) {
        return this.direction = direction.normalize();
    }

    public void goLeft() {
        rotation = Rotation.LEFT;
    }

    public void goRight() {
        rotation = Rotation.RIGHT;
    }

    public void goStraight() {
        rotation = Rotation.NONE;
    }

    public boolean isTurning() {
        return rotation != Rotation.NONE;
    }

    public List<PVector> getParts() {
        return parts;
    }

    
    /**
     * This function helps the Snake to grow.
     * @param grow     the number of elements that are added to the snake
     */
    
    public void grow(int grow) {
        PVector lastElement = parts.get(SIZE - 1);

        for (int i = 1; i <= grow; i++) {
            parts.add(new PVector(lastElement.x, lastElement.y));
        }
    }
}
