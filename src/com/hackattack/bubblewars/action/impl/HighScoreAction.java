package com.hackattack.bubblewars.action.impl;

import processing.core.PConstants;
import processing.core.PFont;
import processing.core.PImage;

import com.hackattack.bubblewars.action.kind.Action;
import com.hackattack.bubblewars.main.Applet;
import com.hackattack.bubblewars.main.Constants;
import com.hackattack.bubblewars.model.User;

public class HighScoreAction extends Action{

	int prevMode;
	
	public HighScoreAction(Applet surface, PImage background, PFont font){
		super(surface,background,font);
	}
	
	public int getNextMode(){
		// TODO
		return Constants.HIGHSCORE_MODE;
	}
	
	public void prepare(int prevMode){
		this.prevMode = prevMode;
	}
	
	private void drawAgainstTimeScore(){
		getSurface().fill(0);
		getSurface().rectMode(PConstants.CORNER);
		getSurface().rect(0, 0, getSurface().soni.depthWidth()/2, getSurface().soni.depthHeight()/2);
		
		getSurface().textFont(getFont());
		int i = 1;
		for(User user : getSurface().getUserPool().getUsers())
		{
			getSurface().fill(user.getColor());
			getSurface().text("Player "+i+" Score: " + user.getScore(), 10, 10+i*Constants.HIGHSCORE_FONT_SIZE);
			i++;
		}
	}

	public void draw(){
		
		// draw player
		getSurface().background(255);
		getSurface().drawPlayer();

		// draw highscore screen
		if(prevMode == Constants.IN_GAME_MODE){
			drawAgainstTimeScore();
		}
	}
}