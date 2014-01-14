package com.hackattack.bubblewars.action.impl;

import java.util.Date;
import java.util.Vector;

import processing.core.PConstants;
import processing.core.PFont;
import processing.core.PImage;

import com.hackattack.bubblewars.action.kind.Action;
import com.hackattack.bubblewars.main.Applet;
import com.hackattack.bubblewars.main.Constants;
import com.hackattack.bubblewars.model.BodyPart;
import com.hackattack.bubblewars.model.Bubble;
import com.hackattack.bubblewars.model.ColorSet;
import com.hackattack.bubblewars.model.User;
import com.hackattack.bubblewars.pools.impl.BubblePool;
import com.hackattack.bubblewars.util.Util;

public class InGameAction extends Action {
	
	Date startTs;
	Date difficultyChangedTs;
	BubblePool bubblePool;
	ColorSet colorSet;
	
	public InGameAction(Applet surface, PImage background, PFont font){
		super(surface,background,font);
		
		this.difficultyChangedTs = getSurface().getCurrentTs();
		
		// initial color-set
		colorSet = new ColorSet(new Vector<Integer>());
		colorSet.addColor();
				
		// pools
		bubblePool = new BubblePool(surface.soni, colorSet);
		getSurface().getUserPool().setColorSet(colorSet);
	}
	
	public void prepare(int prevMode){
		this.startTs = getSurface().getCurrentTs();
		this.difficultyChangedTs = getSurface().getCurrentTs();
		//bubblePool.setTs(getSurface().getCurrentTs());
		for(User user : getSurface().getUserPool().getUsers()){
			user.setTs(getSurface().getCurrentTs());
		}
	}
	
	private void drawBodyPart(BodyPart part){
		// TODO: draw user in Action? (since he has to be drawn in every action)
		float d = (float) (512e3 / part.getPart3d().z)/4;
		getSurface().ellipseMode(PConstants.CENTER);
		float[] fc = Util.decodePartFillColor(part.getColor());
		if(fc.length>3){
			getSurface().fill(fc[0],fc[1],fc[2],fc[3]);
			getSurface().ellipse(part.getPart2d().x, part.getPart2d().y, d, d);
		}
	}
	
	private void drawBubble(Bubble bubble){
		getSurface().ellipseMode(PConstants.CENTER);
		float[] fc = Util.decodeBubbleFillColor(bubble.getColor());
		if(fc.length>3){
			getSurface().fill(fc[0],fc[1],fc[2],fc[3]);
			getSurface().ellipse(bubble.getPos().x, bubble.getPos().y, bubble.getSize(), bubble.getSize());
		}
	}
	
	private void drawHighscores(){
		getSurface().textFont(getFont());
		getSurface().fill(255);
		int i = 0;
		for(User user : getSurface().getUserPool().getUsers())
		{
			getSurface().text("Score: " + user.getScore(), 10, 30+i*Constants.HIGHSCORE_FONT_SIZE);
			i++;
		}
	}
	
	private void determineColorSet(){ 
		if(Math.abs(getSurface().getCurrentTs().getTime() - difficultyChangedTs.getTime()) > Constants.ADD_COLOR_INTERVAL){
			difficultyChangedTs = getSurface().getCurrentTs();
			colorSet.addColor();
		}
	}
	
	public int getNextMode(){
		/*
		for(User user : getSurface().getUserPool().getUsers()){
			if(user.getScore() >= Constants.NEEDED_SCORE){
				return Constants.HIGHSCORE_MODE;
			}
		}
		*/
		
		if(Constants.COUNTER_TIME-Math.round(Math.abs((getSurface().getCurrentTs().getTime()-startTs.getTime()))/1000) <= 0){
			return Constants.HIGHSCORE_MODE;
		}
		
		return Constants.IN_GAME_MODE;
	}
	
	private void drawCountdown(){
		getSurface().textFont(getFont());
		getSurface().fill(255);
		int secs = Constants.COUNTER_TIME-Math.round(Math.abs((getSurface().getCurrentTs().getTime()-startTs.getTime()))/1000);
		getSurface().text("Time: " + secs, 4*getSurface().soni.depthWidth()/5, 30);
	}
	
	public void draw(){
		
		// draw background and player
		getSurface().image(getBackground(),0,0);
		getSurface().drawPlayer();
		
		// draw clock
		drawCountdown();
		
		// draw highscores
		drawHighscores();

		// determine color-set
		determineColorSet();

		// set colors
		bubblePool.verifyColors();
		getSurface().getUserPool().verifyColors();

		// generate bubbles
		bubblePool.generateBubbles();

		int[] userIds = getSurface().soni.getUsers();
		for(int i=0; i<userIds.length; i++){
			if(getSurface().soni.isTrackingSkeleton(userIds[i])){

				getSurface().getUserPool().generateColors(userIds[i]);
				User user = getSurface().getUserPool().getUser(userIds[i]);

				// get positions and draw
				for(BodyPart part : user.getParts()){
					//chooseColor(part);
					getSurface().get2DPosition(part,userIds[i]);
					drawBodyPart(part);
					bubblePool.checkHits(part, user);
				}

				// draw bubbles
				for(Bubble bubble : bubblePool.getBubbles()){
					bubble.move();
					drawBubble(bubble);
				}
			}
		}
	}
}