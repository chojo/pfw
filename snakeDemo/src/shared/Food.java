package shared;

import processing.core.PVector;

public class Food {
	
	public PVector food = null;
	public float posX;
    public float posY;
    static int number = 0;
    int id;
	
	public Food(int windowWidth, int windowHeight) {
		setX(windowWidth);
		setY(windowHeight);
		food = new PVector(this.posX,this.posY);
		setId(number);
		number++;
    }
	
	private void setX(int windowWidth) {
		posX = (float)Math.random()*windowWidth;
	}
	
	public float getX() {
		return food.x;
	}
	
	private void setY(int windowHeight) {
		posY = (float)Math.random()*windowHeight;
	}
	
	public float getY() {
		return food.y;
	}
	
	private void setId(int newId) {
		id = newId;
	}
	
	public int getId() {
		return id;
	}

}
