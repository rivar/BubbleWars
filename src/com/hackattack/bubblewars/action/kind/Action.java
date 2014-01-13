package com.hackattack.bubblewars.action.kind;

import processing.core.PFont;
import processing.core.PImage;

import com.hackattack.bubblewars.main.Applet;

public abstract class Action {

	final Applet surface;
	final PImage background;
	final PFont font;
	int nextMode;
	
	public Action(Applet surface, PImage background, PFont font){
		this.surface = surface;
		this.background = background;
		this.font = font;
	}
	
	public Applet getSurface(){
		return surface;
	}
	
	public PImage getBackground(){
		return background;
	}
	
	public PFont getFont(){
		return font;
	}
	
	public abstract void draw();
	
	public abstract int getNextMode();
	
	public abstract void prepare();
}