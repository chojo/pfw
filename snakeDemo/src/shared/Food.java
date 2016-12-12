package shared;

import java.util.Random;

import processing.core.PVector;

/**
 * Class Food creates numbered food that can be eaten by the snakes.
 */
public class Food extends PVector {
    /* CONSTANTS */
    
    /** Diameter of the food. */
    public static final int SIZE = 10;
    
    /** The snake increases by this growth-factor.  */
    public static final int GROWTH_FACTOR = 2;

    static final Random random = new Random();
    private static final long serialVersionUID = 43L;

    // TODO Add javadoc.
    public Food(float x, float y) { super(x, y); }
    
    // TODO Add javadoc.
    public static Food randomFood(int fieldWidth, int fieldHeight) {
        return new Food(
                random.nextInt(fieldWidth), random.nextInt(fieldHeight));
    }
    
    // TODO Add javadoc.
    public String getMessage() {
        return "feed " + Float.toString(x) + " " + Float.toString(y);
    }
}
