package com.hackattack.bubblewars.action.impl;

import java.util.Iterator;

import processing.core.PConstants;
import processing.core.PFont;
import processing.core.PImage;
import processing.core.PVector;

import com.hackattack.bubblewars.action.kind.Action;
import com.hackattack.bubblewars.main.Applet;
import com.hackattack.bubblewars.main.Constants;
import com.hackattack.bubblewars.model.BodyPart;
import com.hackattack.bubblewars.model.User;
import com.hackattack.bubblewars.util.Util;

public class TemptingBubbleAction extends Action{

	PVector pos;
	float size = Constants.TEMPTING_BUBBLE_INITIAL_SIZE;

	// movement stabilizer determines how many pixels to one direction
	int posXStabilizerCounter = 0;
	int posYStabilizerCounter = 0;

	/*
	 * movement offset is applied to the bubbles as long as
	 * the stabilizer counter is not 0. interval is [-1;1]
	 */
	int posXOffset = 0;
	int posYOffset = 0;

	public TemptingBubbleAction(Applet surface, PImage background, PFont font){
		super(surface, background, font);
		pos = new PVector(getSurface().soni.depthWidth()/2, getSurface().soni.depthHeight()/2);
	}

	public int getNextMode(){
		if(size >= Constants.TEMPTING_BUBBLE_NEEDED_SIZE){
			for(User user : getSurface().getUserPool().getUsers()){
				Iterator<BodyPart> iter = user.getHands().iterator();
				if(iter.hasNext()){
					BodyPart handA = iter.next();
					getSurface().get2DPosition(handA,user.getId());
					if(iter.hasNext()){
						BodyPart handB = iter.next();
						getSurface().get2DPosition(handB, user.getId());
						if(Util.isCloseEnough(handA.getPart2d(), pos, new PVector(size/3,size/3)) ||
								Util.isCloseEnough(handB.getPart2d(), pos, new PVector(size/3,size/3))) {
							// hands smashed bubble --> go to next mode
							return Constants.MENU_MODE;
						}
					}
				}
			}
		}

		// stay in mode
		return Constants.TEMPTING_BUBBLE_MODE;
	}

	private void moveCenter(){
		// TODO: just move in circle...
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

	public void prepare(int prevMode){
		pos = new PVector(getSurface().soni.depthWidth()/2, getSurface().soni.depthHeight()/2);
	}

	private void determineBubbleSize(){
		PVector position = new PVector();
		float optimalDiff = 5000;
		for(User user : getSurface().getUserPool().getUsers()){
			getSurface().soni.getCoM(user.getId(), position);
			if(Math.abs(Constants.OPTIMAL_USER_DISTANCE-position.z)<optimalDiff){
				optimalDiff = Math.abs(Constants.OPTIMAL_USER_DISTANCE-position.z);
			}
		}

		float add = Constants.TEMPTING_BUBBLE_MAX_GAIN/2-optimalDiff/2;
		if(add < 0) add = 0;
		size = Constants.TEMPTING_BUBBLE_INITIAL_SIZE+add;
	}

	public void draw(){

		// draw player
		getSurface().background(255);
		if(size > Constants.TEMPTING_BUBBLE_NEEDED_SIZE){
			getSurface().drawPlayer();
		}

		// determine bubble size
		determineBubbleSize();

		// draw tempting bubble
		getSurface().fill(getSurface().color(0,255,0,180));
		getSurface().ellipseMode(PConstants.CENTER);
		getSurface().ellipse(pos.x,pos.y, size, size);

		// add tempting text
		getSurface().fill(0);
		getSurface().text("Wanna Pop Some Bubbles?", 10, 30);

		// move tempting bubble
		// TODO
		//moveCenter();
	}
}
