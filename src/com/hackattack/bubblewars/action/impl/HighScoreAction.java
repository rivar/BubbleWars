package com.hackattack.bubblewars.action.impl;

import java.util.Date;

import processing.core.PConstants;
import processing.core.PFont;
import processing.core.PImage;

import com.hackattack.bubblewars.action.kind.Action;
import com.hackattack.bubblewars.main.Applet;
import com.hackattack.bubblewars.main.Constants;
import com.hackattack.bubblewars.model.User;

public class HighScoreAction extends Action{

	int prevMode;
	Date startTs;
	Date blinkTs;
	Date finalTs;
	boolean drawBlink = true;
	PImage winnerPic = null;

	public HighScoreAction(Applet surface, PImage background, PFont font){
		super(surface,background,font);
	}

	public int getNextMode(){
		// TODO
		if(finalTs != null){
			if(getSurface().getCurrentTs().getTime()-finalTs.getTime()>Constants.HIGHSCORE_PIC_SHOW*1000){
				return Constants.TEMPTING_BUBBLE_MODE;
			}
		}
		return Constants.HIGHSCORE_MODE;
	}

	public void prepare(int prevMode){
		this.prevMode = prevMode;
		startTs = getSurface().getCurrentTs();
		blinkTs = startTs;
	}

	private void drawAgainstTimeScore(){
		getSurface().stroke(0);
		getSurface().strokeWeight(1);
		getSurface().fill(0);
		getSurface().rectMode(PConstants.CORNER);
		getSurface().rect(0, 0, Constants.WIDTH/2, Constants.HEIGHT/2);

		getSurface().textFont(getFont());
		int i = 1;
		for(User user : getSurface().getUserPool().getUsers())
		{
			getSurface().fill(user.getColor());
			getSurface().text("Player "+i+" Score: " + user.getScore(), 10, 10+i*Constants.HIGHSCORE_FONT_SIZE);
			i++;
		}
	}

	private void drawCamBox(){
		if(drawBlink){
			getSurface().rectMode(PConstants.CORNER);
			getSurface().noFill();
			getSurface().stroke(255,0,0);
			getSurface().strokeWeight(8);
			getSurface().rect(Constants.WIDTH/2+10, 10, Constants.WIDTH/2-10, Constants.HEIGHT-10);
		}

		int secs = Constants.HIGHSCORE_COUNTDOWN-Math.round(Math.abs((getSurface().getCurrentTs().getTime()-startTs.getTime()))/1000);
		getSurface().textFont(getFont());
		getSurface().fill(0);
		getSurface().text(secs+"", 3*Constants.WIDTH/4, Constants.HEIGHT/2);
	}

	public void draw(){
		
		// draw player
		getSurface().image(getBackground(),0,0);
		getSurface().drawPlayer();

		if(getSurface().getCurrentTs().getTime()-blinkTs.getTime() > 350){
			drawBlink = !drawBlink;
			blinkTs = getSurface().getCurrentTs();
		}

		// draw highscore screen
		if(prevMode == Constants.IN_GAME_MODE){
			drawAgainstTimeScore();
		}
		
		if(getSurface().getCurrentTs().getTime()-startTs.getTime() <= Constants.HIGHSCORE_COUNTDOWN*1000){
			drawCamBox();
		}
		else{
			if(winnerPic == null){
				winnerPic = getSurface().get();
				winnerPic = winnerPic.get(Constants.WIDTH/2+10, 10, Constants.WIDTH/2-10, Constants.HEIGHT-10);
				winnerPic.resize(Constants.WIDTH, Constants.HEIGHT);
				finalTs = getSurface().getCurrentTs();
			}
			getSurface().image(winnerPic,0,0);
		}
	}
}