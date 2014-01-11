package com.hackattack.bubblewars.model;
import java.util.Date;

import processing.core.PVector;


public class BodyPart {
	PVector part2d = new PVector();
	PVector part3d = new PVector();
	final int partId;
	
	// 0: red 1: green 2: blue 3: yellow
	int color;
	Date colorTs;
	
	
	public BodyPart(int partId){
		this.partId = partId;
		colorTs = new Date();
	}

	public PVector getPart2d() {
		return part2d;
	}

	public void setPart2d(PVector part2d) {
		this.part2d = part2d;
	}

	public PVector getPart3d() {
		return part3d;
	}

	public void setPart3d(PVector part3d) {
		this.part3d = part3d;
	}
	
	public int getPartId(){
		return partId;
	}
	
	public int getColor(){
		return color;
	}
	
	public void setColor(int color){
		colorTs = new Date();
		this.color = color;
	}

	public Date getColorTs() {
		return colorTs;
	}

	public void setColorTs(Date colorTs) {
		this.colorTs = colorTs;
	}
}
