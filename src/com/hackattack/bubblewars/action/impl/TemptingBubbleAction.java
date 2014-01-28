package com.hackattack.bubblewars.action.impl;

import java.util.Date;

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
	boolean tooClose = false;
	
	BodyPart lockedPart;
	User lockedUser;
	Date selTs;

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

	public TemptingBubbleAction(Applet surface, PImage background, PFont font){
		super(surface, background, font);
		//pos = new PVector(getSurface().soni.depthWidth()/2, getSurface().soni.depthHeight()/2);
		pos = new PVector(Constants.WIDTH/2, Constants.HEIGHT/2);
	}
	
	private void setLock(BodyPart lockedPart, User user){
		this.lockedPart = lockedPart;
		this.lockedUser = user;
	}

	private void unlock(){
		lockedPart = null;
		lockedUser = null;
		selTs = null;
	}
	
	private void checkHit(){
		if(size < Constants.TEMPTING_BUBBLE_NEEDED_SIZE) {
			unlock();
			return;
		}
		if(lockedPart == null){
			for(User user : getSurface().getUserPool().getUsers()){
				for(BodyPart hand : user.getHands()){
					// locate hands position
					getSurface().get2DPosition(hand,user.getId());

					if(Util.isCloseEnough(pos, hand.getPart2d(), new PVector(size+sizeDeltaX/2,size+sizeDeltaY/2))){
						// first time that hand is in range
						selTs = getSurface().getCurrentTs();
						setLock(hand,user);
						return;
					}
				}
			}
		}
		else{
			checkLockedPart();
		}
	}

	private void checkLockedPart(){
		// locate hand
		getSurface().get2DPosition(lockedPart,lockedUser.getId());
		if(!Util.isCloseEnough(lockedPart.getPart2d(), pos, new PVector(size+sizeDeltaX/2,size+sizeDeltaY/2))){
			unlock();
		}
	}

	public int getNextMode(){
		if(size >= Constants.TEMPTING_BUBBLE_NEEDED_SIZE){
			/*
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
							// hands smashed bubble
							return Constants.MENU_MODE;
						}
					}
				}
			}
			*/
			
			if(selTs != null){
				if(Math.abs(selTs.getTime() - getSurface().getCurrentTs().getTime())>=Constants.TEMPTING_BUBBLE_SMASH_TIME*1000){
					return Constants.MENU_MODE;
				}
			}
		}

		// stay in mode
		return Constants.TEMPTING_BUBBLE_MODE;
	}

	private void resizeBubble(){
		boolean useX=(Util.random(new Integer(0), new Integer(2))==0);
		if(useX) resizeX();
		else resizeY();
	}

	private void resizeY(){
		if(isGrowingY){
			sizeDeltaY++;
			if(sizeDeltaY >= Constants.MAX_DELTA_Y){
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
			if(sizeDeltaX >= Constants.MAX_DELTA_X){
				isGrowingX = false;
			}
		}else{
			sizeDeltaX--;
			if(sizeDeltaX <= 0){
				isGrowingX = true;
			}
		}
	}

	private void moveCenter(){
		if(lockedPart != null) return;
		// TODO: just move in circle...
		// TODO avoid running out of the frame not that dirty
		//if(pos.x < 1*getSurface().soni.depthWidth()/3 || pos.x > 2*getSurface().soni.depthWidth()/3 || pos.y < 1*getSurface().soni.depthHeight()/3 || pos.y > 2*getSurface().soni.depthHeight()/3){
		if(pos.x < 1*Constants.WIDTH/3 || pos.x > 2*Constants.HEIGHT/3 || pos.y < 1*Constants.WIDTH/3 || pos.y > 2*Constants.HEIGHT/3){
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
		//pos = new PVector(getSurface().soni.depthWidth()/2, getSurface().soni.depthHeight()/2);
		pos = new PVector(Constants.WIDTH/2, Constants.HEIGHT/2);
	}

	private void determineBubbleSize(){
		PVector position = new PVector();
		float optimalDiff = 5000;
		for(User user : getSurface().getUserPool().getUsers()){
			getSurface().soni.getCoM(user.getId(), position);
			if(Math.abs(Constants.OPTIMAL_USER_DISTANCE-position.z)<optimalDiff){
				if(Constants.OPTIMAL_USER_DISTANCE-position.z < 0){
					tooClose = false;
				}else{
					tooClose = true;
				}
				optimalDiff = Math.abs(Constants.OPTIMAL_USER_DISTANCE-position.z);
			}
		}

		float add = Constants.TEMPTING_BUBBLE_MAX_GAIN/2-optimalDiff/2;
		if(add < 0) add = 0;
		size = Constants.TEMPTING_BUBBLE_INITIAL_SIZE+add;
	}
	
	private void drawText(){
		getSurface().fill(255);
		getSurface().textFont(getSurface().createFont("Arial", Constants.TEMPTING_FONT_SIZE, true));
		//int xPos = getSurface().soni.depthWidth()/5;
		//int yPos = getSurface().soni.depthHeight()-30;
		int xPos = Constants.WIDTH/4;
		int yPos = Constants.HEIGHT-50;
		if(tooClose && size < Constants.TEMPTING_BUBBLE_NEEDED_SIZE){
			getSurface().text("Step back a little", xPos, yPos);
		}else if(size < Constants.TEMPTING_BUBBLE_NEEDED_SIZE){
			getSurface().text("Come a little closer",xPos,yPos);
		}else{
			getSurface().text("Perfect distance",xPos,yPos);
		}
	}

	public void draw(){

		// draw player
		getSurface().image(getBackground(),0,0);
		getSurface().drawPlayer();

		// determine bubble size
		determineBubbleSize();
		
		// draw tempting text
		drawText();
		
		// check hit
		checkHit();

		// draw tempting bubble
		if(size >= Constants.TEMPTING_BUBBLE_NEEDED_SIZE){
			getSurface().fill(getSurface().color(0,255,0,180));
		}else{
			getSurface().fill(getSurface().color(255,0,0,180));
		}
		getSurface().ellipseMode(PConstants.CENTER);
		getSurface().ellipse(pos.x,pos.y, size+sizeDeltaX, size+sizeDeltaY);

		// add tempting text
		getSurface().fill(255);
		getSurface().textFont(getSurface().createFont("Arial", Constants.TEMPTING_FONT_SIZE, true));
		//getSurface().text("Wanna Pop Some Bubbles?", getSurface().soni.depthWidth()/5, 30);
		getSurface().text("Wanna Pop Some Bubbles?", Constants.WIDTH/4, 50);
		
		// text
		if(selTs != null){
			getSurface().textFont(getFont());
			getSurface().fill(0);
			int secs = Math.round(Constants.TEMPTING_BUBBLE_SMASH_TIME-Math.round(Math.abs((getSurface().getCurrentTs().getTime()-selTs.getTime()))/1000));
			getSurface().text(secs, pos.x-10, pos.y+10);
		}
		
		// move tempting bubble
		moveCenter();
		resizeBubble();
	}
}
