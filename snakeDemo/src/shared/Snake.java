package shared;

import processing.core.PVector;

import java.util.LinkedList;
import java.util.List;

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
}
