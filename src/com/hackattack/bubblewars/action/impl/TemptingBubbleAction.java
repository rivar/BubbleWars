package com.hackattack.bubblewars.action.impl;

import java.util.Iterator;

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

	PVector bubbleCenter;
	
	public TemptingBubbleAction(Applet surface, PImage background, PFont font){
		super(surface, background, font);
		bubbleCenter = new PVector(getSurface().soni.depthWidth()/2, getSurface().soni.depthHeight()/2);
	}
	
	public int getNextMode(){
		for(User user : getSurface().getUserPool().getUsers()){
			Iterator<BodyPart> iter = user.getHands().iterator();
			if(iter.hasNext()){
				BodyPart handA = iter.next();
				getSurface().get2DPosition(handA,user.getId());
				if(iter.hasNext()){
					BodyPart handB = iter.next();
					getSurface().get2DPosition(handB, user.getId());
					if(Util.isCloseEnough(handA.getPart2d(), bubbleCenter, Constants.BODYPART_TEMPTING_MINIMUM_DISTANCE) &&
							Util.isCloseEnough(handB.getPart2d(), bubbleCenter, Constants.BODYPART_TEMPTING_MINIMUM_DISTANCE)) {
						// hands smashed bubble --> go to next mode
						return Constants.IN_GAME_MODE;
					}
				}
			}
		}
		
		// stay in mode
		return Constants.TEMPTING_BUBBLE_MODE;
	}
	
	private void moveCenter(){
		// TODO
	}
	
	public void prepare(){
		bubbleCenter = new PVector(getSurface().soni.depthWidth()/2, getSurface().soni.depthHeight()/2);
	}
	
	public void draw(){
		getSurface().background(255);
		getSurface().drawPlayer();
		
		// draw tempting bubble
		getSurface().fill(0);
		getSurface().ellipse(bubbleCenter.x,bubbleCenter.y, 50, 50);
		
		// add tempting text
		getSurface().text("Wanna Pop Some Bubbles?", 10, 30);
		
		// move tempting bubble
		moveCenter();
	}
}
