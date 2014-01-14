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

	public MenuAction(Applet surface, PImage background, PFont font){
		super(surface,background,font);
	}

	private void moveMenu(PVector vector){
		// TODO
	}

	public void prepare(int prevMode){
		// TODO
		// set initial position
		againstTimeMenu = new PVector(getSurface().soni.depthWidth()/3,getSurface().soni.depthHeight()/3);
		survivalMenu = new PVector(2*getSurface().soni.depthWidth()/3,2*getSurface().soni.depthHeight()/3);

		// set ts
		againstTimeTs = null;
		survivalTs = null;
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

	public void draw(){

		// draw player
		getSurface().background(255);
		getSurface().drawPlayer();

		// check hit
		checkHit();

		// slightly move menus
		moveMenu(againstTimeMenu);
		moveMenu(survivalMenu);

		// draw menus
		getSurface().textFont(getFont());
		getSurface().ellipseMode(PConstants.CENTER);
		getSurface().fill(getSurface().color(0,255,0,80));
		getSurface().ellipse(againstTimeMenu.x, againstTimeMenu.y, Constants.MENU_SIZE, Constants.MENU_SIZE);
		getSurface().fill(0);
		getSurface().text("Against The Time", againstTimeMenu.x-Constants.MENU_SIZE/2, againstTimeMenu.y-Constants.MENU_SIZE/2-10);
		getSurface().text("Survival", survivalMenu.x-Constants.MENU_SIZE/2, survivalMenu.y-Constants.MENU_SIZE/2-10);
		getSurface().fill(getSurface().color(255,0,0,80));
		getSurface().ellipse(survivalMenu.x, survivalMenu.y, Constants.MENU_SIZE, Constants.MENU_SIZE);

		// draw choice clock
		if(againstTimeTs != null){
			int secs = Constants.MENU_CHOICE_TIME-Math.round(Math.abs((getSurface().getCurrentTs().getTime()-againstTimeTs.getTime()))/1000);
			// draw circle
			getSurface().fill(100);
			getSurface().ellipse(againstTimeMenu.x, againstTimeMenu.y, Constants.MENU_SIZE, Constants.MENU_SIZE);

			// draw text
			getSurface().textFont(getFont());
			//getSurface().textMode(PConstants.CENTER);
			getSurface().fill(255);
			getSurface().text(secs+"", againstTimeMenu.x, againstTimeMenu.y);
		}
		else if(survivalTs != null){
			int secs = Constants.MENU_CHOICE_TIME-Math.round(Math.abs((getSurface().getCurrentTs().getTime()-survivalTs.getTime()))/1000);
			// draw circle
			getSurface().fill(100);
			getSurface().ellipse(survivalMenu.x, survivalMenu.y, Constants.MENU_SIZE, Constants.MENU_SIZE);

			// draw text
			getSurface().textFont(getFont());
			//getSurface().textMode(PConstants.CENTER);
			getSurface().fill(255);
			getSurface().text(secs+"", survivalMenu.x, survivalMenu.y);
		}
	}
}