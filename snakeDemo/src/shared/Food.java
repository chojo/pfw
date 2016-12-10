package shared;

import java.util.Random;

import processing.core.PVector;

public class Food extends PVector {
    static final Random random = new Random();
    private static final long serialVersionUID = 43L;

    public Food(int fieldWidth, int fieldHeight) {
        super(random.nextInt(fieldWidth), random.nextInt(fieldHeight));
    }
}
