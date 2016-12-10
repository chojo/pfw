package shared;

import java.util.Random;

import processing.core.PVector;

public class Food extends PVector {
    static final Random random = new Random();
    private static final long serialVersionUID = 43L;

    public Food(int x, int y) { super(x, y); }

    public static Food randomFood(int fieldWidth, int fieldHeight) {
        return new Food(
                random.nextInt(fieldWidth), random.nextInt(fieldHeight));
    }

    public String getMessage() {
        return "feed " + Float.toString(x) + " " + Float.toString(y);
    }
}
