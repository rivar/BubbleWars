import processing.core.PVector;


public class Bubble {

	PVector pos;
	int color = 0;
	int size;
	int points = 10; // TODO....
	
	
	public void move(){
		// TODO
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
