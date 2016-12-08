package shared;

import client.SnakeTest;
import processing.core.PVector;

import java.util.LinkedList;
import java.util.List;

import static processing.core.PApplet.println;

public class Snake {
    private int size = 20;
    private List<PVector> parts = new LinkedList<>();

    public Snake(float x, float y) {
        for (int i=0; i< size; i++) {
            parts.add(new PVector(x,y));
        }
    }

    public void moveTo(PVector newHead) {
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

    public void moveBy(PVector direction) {
        moveTo(PVector.add(head(), direction));
    }

    public PVector head() {
        return parts.get(0);
    }


    public List<PVector> getParts() {
        return parts;
    }

    public void grow(int grow) {
        PVector lastElement = parts.get(size - 1);

        for (int i = 1; i <= grow; i++) {
            parts.add(new PVector(lastElement.x, lastElement.y));
        }
    }
}
