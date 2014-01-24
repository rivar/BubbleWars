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

public class MenuAction extends Action{

	PVector againstTimeMenu;
	PVector survivalMenu;

	Date againstTimeTs;
	Date survivalTs;

	BodyPart lockedPart;
	User lockedUser;
	PVector chosenMenu;

	
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
	
	
	public MenuAction(Applet surface, PImage background, PFont font){
		super(surface,background,font);
	}

	public void prepare(int prevMode){
		// TODO
		// set initial position
		againstTimeMenu = new PVector(Constants.WIDTH/3,Constants.HEIGHT/3);
		survivalMenu = new PVector(2*Constants.WIDTH/3,2*Constants.HEIGHT/3);

		// set ts
		againstTimeTs = null;
		survivalTs = null;
	}
	
	private void moveMenu(PVector pos){
		if(pos == chosenMenu) return;
		
		// TODO: just move in circle...
		// TODO avoid running out of the frame not that dirty
		/*
		if(pos.x < 1*getSurface().soni.depthWidth()/3 || pos.x > 2*getSurface().soni.depthWidth()/3 || pos.y < 1*getSurface().soni.depthHeight()/3 || pos.y > 2*getSurface().soni.depthHeight()/3){
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
		*/
	}

	public int getNextMode(){
		if(againstTimeTs != null){
			if(Math.abs((getSurface().getCurrentTs().getTime()-againstTimeTs.getTime())/1000) >= Constants.MENU_CHOICE_TIME){
				return Constants.IN_GAME_MODE;
			}
		}
		else if(survivalTs != null){
			if(Math.abs((getSurface().getCurrentTs().getTime()-survivalTs.getTime())/1000) >= Constants.MENU_CHOICE_TIME){
				return Constants.IN_GAME_MODE;
			}
		}

		return Constants.MENU_MODE;
	}

	private void setLock(BodyPart lockedPart, PVector menu, User user){
		this.lockedPart = lockedPart;
		this.chosenMenu = menu;
		this.lockedUser = user;
	}

	private void unlock(){
		lockedPart = null;
		lockedUser = null;
		chosenMenu = null;
		againstTimeTs = null;
		survivalTs = null;

	}

	private void checkHit(){
		if(lockedPart == null){
			for(User user : getSurface().getUserPool().getUsers()){
				for(BodyPart hand : user.getHands()){
					// locate hands position
					getSurface().get2DPosition(hand,user.getId());

					if(Util.isCloseEnough(againstTimeMenu, hand.getPart2d(), new PVector(Constants.MENU_SIZE/2,Constants.MENU_SIZE/2))){
						// first time that hand is in range
						againstTimeTs = getSurface().getCurrentTs();
						setLock(hand,againstTimeMenu,user);
						return;
					}
					else if(Util.isCloseEnough(survivalMenu, hand.getPart2d(), new PVector(Constants.MENU_SIZE/2,Constants.MENU_SIZE/2))){
						// first time that hand is in range
						survivalTs = getSurface().getCurrentTs();
						setLock(hand,survivalMenu,user);
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
		if(!Util.isCloseEnough(lockedPart.getPart2d(), chosenMenu, new PVector(Constants.MENU_SIZE/2,Constants.MENU_SIZE/2))){
			unlock();
		}
	}
	
	private void resizeMenus(){
		boolean useX=(Util.random(new Integer(0), new Integer(2))==0);
		if(useX) resizeX();
		else resizeY();
	}

	private void resizeY(){
		if(isGrowingY){
			sizeDeltaY++;
			if(sizeDeltaY >= Constants.MENU_SIZE/3){
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
			if(sizeDeltaX >= Constants.MENU_SIZE/3){
				isGrowingX = false;
			}
		}else{
			sizeDeltaX--;
			if(sizeDeltaX <= 0){
				isGrowingX = true;
			}
		}
	}

	public void draw(){

		// draw player
		getSurface().image(getBackground(),0,0);
		getSurface().drawPlayer();

		// check hit
		checkHit();

		// slightly move menus
		moveMenu(againstTimeMenu);
		moveMenu(survivalMenu);
		resizeMenus();
		
		// draw menus
		getSurface().textFont(getFont());
		getSurface().ellipseMode(PConstants.CENTER);
		getSurface().fill(getSurface().color(255,0,0,80));
		getSurface().ellipse(againstTimeMenu.x, againstTimeMenu.y, Constants.MENU_SIZE+sizeDeltaX, Constants.MENU_SIZE+sizeDeltaY);
		getSurface().fill(255);
		getSurface().text("Against The Time", againstTimeMenu.x-Constants.MENU_SIZE/2, againstTimeMenu.y-Constants.MENU_SIZE/2-10);
		getSurface().text("Survival", survivalMenu.x-Constants.MENU_SIZE/2, survivalMenu.y-Constants.MENU_SIZE/2-10);
		getSurface().fill(getSurface().color(255,0,0,80));
		getSurface().ellipse(survivalMenu.x, survivalMenu.y, Constants.MENU_SIZE+sizeDeltaX, Constants.MENU_SIZE+sizeDeltaY);

		// draw choice clock
		if(againstTimeTs != null){
			int secs = Constants.MENU_CHOICE_TIME-Math.round(Math.abs((getSurface().getCurrentTs().getTime()-againstTimeTs.getTime()))/1000);
			// draw circle
			getSurface().fill(getSurface().color(0,255,0));
			getSurface().ellipse(againstTimeMenu.x, againstTimeMenu.y, Constants.MENU_SIZE+sizeDeltaX, Constants.MENU_SIZE+sizeDeltaY);

			// draw text
			getSurface().textFont(getFont());
			//getSurface().textMode(PConstants.CENTER);
			getSurface().fill(0);
			getSurface().text(secs+"", againstTimeMenu.x-10, againstTimeMenu.y+10);
		}
		else if(survivalTs != null){
			int secs = Constants.MENU_CHOICE_TIME-Math.round(Math.abs((getSurface().getCurrentTs().getTime()-survivalTs.getTime()))/1000);
			// draw circle
			getSurface().fill(getSurface().color(0,255,0));
			getSurface().ellipse(survivalMenu.x, survivalMenu.y, Constants.MENU_SIZE+sizeDeltaX, Constants.MENU_SIZE+sizeDeltaY);

			// draw text
			getSurface().textFont(getFont());
			//getSurface().textMode(PConstants.CENTER);
			getSurface().fill(0);
			getSurface().text(secs+"", survivalMenu.x-10, survivalMenu.y+10);
		}
	}
}