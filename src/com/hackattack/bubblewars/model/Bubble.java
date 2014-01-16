package com.hackattack.bubblewars.model;
import java.util.Date;

import processing.core.PVector;

import com.hackattack.bubblewars.util.Util;

public class Bubble {

	PVector pos;
	int color = 0;
	int size;
	int points = 10; // TODO....
	
	Date hitTs;
	
	boolean isGrowingX = true;
	boolean isGrowingY = true;
	int sizeDeltaX = 0;
	int sizeDeltaY = 0;
	
	// movement stabilizer determines how many pixels to one direction
	int posXStabilizerCounter = 0;
	int posYStabilizerCounter = 0;

	/*
	 * movement offset is applied to the bubbles as long as
	 * the stabilizer counter is not 0. interval is [-1;1]
	 */
	int posXOffset = 0;
	int posYOffset = 0;
	
	public void resizeBubble(){
		boolean useX=(Util.random(new Integer(0), new Integer(2))==0);
		if(useX) resizeX();
		else resizeY();
	}

	private void resizeY(){
		if(isGrowingY){
			sizeDeltaY++;
			if(sizeDeltaY >= size/3){
				isGrowingY = false;
			}
		}else{
			sizeDeltaY--;
			if(sizeDeltaY <= 0){
				isGrowingY = true;
			}
		}
	}

	private void resizeX(){
		if(isGrowingX){
			sizeDeltaX++;
			if(sizeDeltaX >= size/3){
				isGrowingX = false;
			}
		}else{
			sizeDeltaX--;
			if(sizeDeltaX <= 0){
				isGrowingX = true;
			}
		}
	}
	
	public void move(){
		// TODO avoid running out of the frame not that dirty
		if(pos.x < 0 || pos.x > 640 || pos.y < 0 || pos.y > 480){
			posXStabilizerCounter = 0;
			posYStabilizerCounter = 0;
		}
		
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
	
	public Bubble(int size, int points){
		this.size = size;
		this.points = points;
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
		// TODO
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
	
	public int getDeltaX(){
		return sizeDeltaX;
	}
	
	public int getDeltaY(){
		return sizeDeltaY;
	}

	public Date getHitTs() {
		return hitTs;
	}

	public void setHitTs(Date hitTs) {
		this.hitTs = hitTs;
	}
	
	
}
