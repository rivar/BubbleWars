package com.hackattack.bubblewars.main;

import java.util.Date;
import java.util.HashMap;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
import SimpleOpenNI.SimpleOpenNI;

import com.hackattack.bubblewars.action.impl.InGameAction;
import com.hackattack.bubblewars.action.impl.TemptingBubbleAction;
import com.hackattack.bubblewars.action.kind.Action;
import com.hackattack.bubblewars.model.BodyPart;
import com.hackattack.bubblewars.pools.impl.UserPool;


public class Applet extends PApplet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// openNI
	public SimpleOpenNI soni;
	
	// stamps
	Date appStartTs;
	Date currentTs;
	
	// pools
	UserPool userPool;
	
	// action mapping
	HashMap<Integer, Action> actionMap = new HashMap<Integer, Action>();
	int currentMode;
	
	public void setup(){
		// timestamps
		appStartTs= new Date();
		currentTs = appStartTs;
		
		// camera setup
		soni = new SimpleOpenNI(this);
		soni.setMirror(true);
		soni.enableDepth();
		soni.enableUser();
		smooth();
		size(soni.depthWidth(), soni.depthHeight());
		
		// setup pool
		userPool = new UserPool();
		
		// ======= InGameAction ========
		PImage bgInGame = loadImage("background_in_game.jpg");
		bgInGame.resize(soni.depthWidth(), soni.depthHeight());
		
		// screen font
		PFont fontInGame = createFont("Arial",Constants.HIGHSCORE_FONT_SIZE,true);
		
		// add action
		actionMap.put(Constants.IN_GAME_MODE,new InGameAction(this,bgInGame,fontInGame));
		
		// ======= TemptingBubbleAction =========
		PImage bgTempt = loadImage("background_in_game.jpg");
		bgTempt.resize(soni.depthWidth(), soni.depthHeight());
		
		// screen font
		PFont fontTempt = createFont("Arial",Constants.HIGHSCORE_FONT_SIZE,true);
		
		// add action
		actionMap.put(Constants.TEMPTING_BUBBLE_MODE,new TemptingBubbleAction(this,bgTempt,fontTempt));
		
		
		
		// set entry mode
		currentMode = Constants.TEMPTING_BUBBLE_MODE;
	}
	
	public void get2DPosition(BodyPart part, int userId){
		
		// get body coords
		soni.getJointPositionSkeleton(userId, part.getPartId(), part.getPart3d());
		soni.convertRealWorldToProjective(part.getPart3d(), part.getPart2d());
	}
	
	private boolean allowAddUser(){
		// TODO
		return true;
	}
	
	private boolean allowRemoveUser(){
		// TODO
		return true;
	}
	
	public void onNewUser(SimpleOpenNI context, int userId){
		if(allowAddUser()){
			context.startTrackingSkeleton(userId);
			userPool.addUser(userId);
		}
	}
	
	public void onLostUser(SimpleOpenNI context, int userId){
		if(allowRemoveUser()){
			userPool.removeUser(userId);
		}
	}
	
	public void drawPlayer(){
		//image(soni.userImage(),0,0);
		PImage playerImg = soni.depthImage();
		playerImg.loadPixels();
		//int[] pix = playerImg.pixels;
		loadPixels();

		// set pixels
		int[] userMap;
		if(soni.getNumberOfUsers() > 0){
			userMap = soni.userMap();
			//loadPixels();
			for(int i=0; i < userMap.length; i++){
				if(userMap[i] != 0){
					// user TODO: different coloring per user?
					pixels[i] = playerImg.pixels[i]+100;
				}
				else{
					// background
					//pixels[i] = color(0,0,0);
				}
			}
			updatePixels();
		}
	}
	
	public void draw(){
		// update timestamp
		currentTs = new Date();
		
		// update camera frame
		soni.update();
		 
		// draw depending on mode
		actionMap.get(currentMode).draw();
		
		// get the next mode
		int nextMode = actionMap.get(currentMode).getNextMode();
		if(nextMode != currentMode){
			actionMap.get(nextMode).prepare();
			currentMode = nextMode;
		}
	}
	
	public UserPool getUserPool(){
		return userPool;
	}
	
	public Date getCurrentTs(){
		return currentTs;
	}
	
	public Date getStartTs(){
		return appStartTs;
	}
}