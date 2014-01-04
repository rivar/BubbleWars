import processing.core.PVector;

public class Bubble {

	PVector pos;
	int color = 0;
	int size;
	int points = 10; // TODO....
	
	// movement stabilizer determines how many pixels to one direction
	int posXStabilizerCounter = 0;
	int posYStabilizerCounter = 0;

	/*
	 * movement offset is applied to the bubbles as long as
	 * the stabilizer counter is not 0. interval is [-1;1]
	 */
	int posXOffset = 0;
	int posYOffset = 0;
	
	public void move(){
		// TODO avoid running out of the frame
		// TODO check for other bubbles to avoid collisions
		// TODO play with max/min values
		if(posXStabilizerCounter == 0){
			// generate int values of interval [min;max]: Min + (int)(Math.random() * ((Max - Min) + 1))
			posXStabilizerCounter = 10 + (int)(Math.random()*41);
			posXOffset = -1 + (int)(Math.random() * 3);
		} else{
			posXStabilizerCounter--;
		}
		if(posYStabilizerCounter == 0){
			posYStabilizerCounter = 10 + (int)(Math.random()*41);
			posYOffset = -1 + (int)(Math.random() * 3);
		} else{
			posYStabilizerCounter--;
		}
		pos.set(pos.x+posXOffset, pos.y+posYOffset);
	}
	
	public Bubble(int size){
		this.size = size;
	}
	
	public PVector getPos() {
		return pos;
	}
	public void setPos(PVector pos) {
		this.pos = pos;
	}
	public int getColor() {
		return color;
	}
	public void setColor(int color) {
		this.color = color;
	}
	public int getPoints() {
		return points;
	}
	public void setPoints(int points) {
		this.points = points;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
}
