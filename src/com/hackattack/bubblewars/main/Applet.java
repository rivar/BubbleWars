package com.hackattack.bubblewars.main;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
import SimpleOpenNI.SimpleOpenNI;

import com.hackattack.bubblewars.action.impl.HighScoreAction;
import com.hackattack.bubblewars.action.impl.InGameAction;
import com.hackattack.bubblewars.action.impl.MenuAction;
import com.hackattack.bubblewars.action.impl.TemptingBubbleAction;
import com.hackattack.bubblewars.action.kind.Action;
import com.hackattack.bubblewars.model.BodyPart;
import com.hackattack.bubblewars.model.User;
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
	
	// colors
	List<Integer> userColorSet = new ArrayList<Integer>();
	List<Integer> usedUserColors = new ArrayList<Integer>();

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

		// setup user pool
		userPool = new UserPool();
		
		// setup user colors
		userColorSet.add(color(255,0,0));
		userColorSet.add(color(0,255,0));
		userColorSet.add(color(0,0,255));

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

		// ======= MenuAction =========
		PImage bgMenu = loadImage("background_in_game.jpg");
		bgMenu.resize(soni.depthWidth(), soni.depthHeight());

		// screen font
		PFont fontMenu = createFont("Arial",Constants.HIGHSCORE_FONT_SIZE,true);

		// add action
		actionMap.put(Constants.MENU_MODE,new MenuAction(this,bgMenu,fontMenu));

		// ======= HighScoreAction =========
		PImage bgScore = loadImage("background_in_game.jpg");
		bgScore.resize(soni.depthWidth(), soni.depthHeight());

		// screen font
		PFont fontScore = createFont("Arial",Constants.HIGHSCORE_FONT_SIZE,true);

		// add action
		actionMap.put(Constants.HIGHSCORE_MODE,new HighScoreAction(this,bgScore,fontScore));



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
		if(currentMode == Constants.IN_GAME_MODE || userPool.getUsers().size() >= Constants.ALLOWED_USERS){
			return false;
		}
		else{
			return true;
		}
	}

	private boolean allowRemoveUser(){
		if(currentMode == Constants.IN_GAME_MODE){
			return false;
		}
		else{
			return true;
		}
	}
	
	private int getNextUserColor(){
		Iterator<Integer> iter = userColorSet.iterator();
		while(iter.hasNext()){
			Integer cur = iter.next();
			if(!usedUserColors.contains(cur)){
				usedUserColors.add(cur);
				return cur;
			}
		}
		
		// default
		return userColorSet.get(0);
	}

	public void onNewUser(SimpleOpenNI context, int userId){
		
		// TODO: color depth effect
		if(allowAddUser()){
			System.out.println("USER ADDED");
			context.startTrackingSkeleton(userId);
			userPool.addUser(userId,getNextUserColor());
		}
	}

	public void onLostUser(SimpleOpenNI context, int userId){
		if(allowRemoveUser()){
			System.out.println("USER LEFT");
			usedUserColors.remove(userPool.getUser(userId).getColor());
			userPool.removeUser(userId);
		}
	}

	public void drawPlayer(){

		//image(soni.userImage(),0,0);
		//PImage playerImg = soni.depthImage();
		//playerImg.loadPixels();
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
					//pixels[i] = playerImg.pixels[i];
					User user = userPool.getUser(userMap[i]);
					if(user != null){
						pixels[i] = user.getColor();
					}
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
			actionMap.get(nextMode).prepare(currentMode);
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