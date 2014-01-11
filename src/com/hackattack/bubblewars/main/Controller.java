package com.hackattack.bubblewars.main;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
import processing.core.PVector;
import SimpleOpenNI.SimpleOpenNI;

import java.util.ArrayList;
import java.util.Date;

import com.hackattack.bubblewars.model.BodyPart;
import com.hackattack.bubblewars.model.Bubble;
import com.hackattack.bubblewars.model.User;
import com.hackattack.bubblewars.pools.BubblePool;
import com.hackattack.bubblewars.pools.UserPool;
import com.hackattack.bubblewars.util.Util;

public class Controller extends PApplet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public SimpleOpenNI soni;
	
	Date startTs;
	Date currentTs;
	PFont font;
	PImage backgroundImage = null;
	BubblePool bubblePool;
	UserPool userPool;
	
	
	public void setup(){
		// ts
		startTs= new Date();
		currentTs = startTs;
		
		// cam setup
		soni = new SimpleOpenNI(this);
		soni.setMirror(true);
		soni.enableDepth();
		soni.enableUser();
		smooth();
		
		// pools
		bubblePool = new BubblePool(soni);
		userPool = new UserPool();
		
		size(soni.depthWidth(), soni.depthHeight());
		
		backgroundImage = loadImage("background_in_game.jpg");
		backgroundImage.resize(soni.depthWidth(), soni.depthHeight());
		
		// screen font
		font = createFont("Arial",Constants.HIGHSCORE_FONT_SIZE,true);
	}
	
	public void onNewUser(SimpleOpenNI context, int userId){
		//context.setMirror(true);
		context.startTrackingSkeleton(userId);
		userPool.addUser(userId);
	}
	
	public void onLostUser(SimpleOpenNI context, int userId){
		userPool.removeUser(userId);
	}
	
	private void drawBodyPart(BodyPart part){
		float d = (float) (512e3 / part.getPart3d().z)/4;
		ellipseMode(CENTER);
		float[] fc = Util.determineFillColor(part.getColor());
		if(fc.length>3){
			fill(fc[0],fc[1],fc[2],fc[3]);
			ellipse(part.getPart2d().x, part.getPart2d().y, d, d);
		}
	}
	
	private void get2DPositionAndDraw(BodyPart part, int userId){
		
		// get body coords
		soni.getJointPositionSkeleton(userId, part.getPartId(), part.getPart3d());
		soni.convertRealWorldToProjective(part.getPart3d(), part.getPart2d());
		
		drawBodyPart(part);
	}
	
	private void chooseColor(BodyPart part){
		if(currentTs.getTime() - part.getColorTs().getTime() > Constants.START_COLOR_INTERVAL){
			// TODO: Interval [-0.5,3.5] 
			part.setColor((int)Math.round(random(0, Constants.NUM_COLORS-1)));
		}
	}
	
	private void drawBubble(Bubble bubble){
		ellipseMode(CENTER);
		float[] fc = Util.determineFillColor(bubble.getColor());
		if(fc.length>3){
			fill(fc[0],fc[1],fc[2],fc[3]);
			ellipse(bubble.getPos().x, bubble.getPos().y, bubble.getSize(), bubble.getSize());
		}
	}
	
	private void drawHighscore(User user){
		textFont(font);
		fill(255);
		// TODO display highscore for multiple player
		text("Score: " + user.getScore(), 10, 30);
	}
	
	public void draw(){
		//background(0);
		//imageMode(CORNER);
		image(backgroundImage,0,0);
		// update ts
		currentTs = new Date();
		
		//if(currentTs.getTime() - startTs.getTime() > 60000){
		//	System.out.println("Shutdown system after 60s to prevent system crash :P");
		//	System.exit(CLOSE);
		//}
		
		// generate bubbles
		bubblePool.generateBubbles(currentTs);
		
		// update cam info
		soni.update();
		//image(soni.userImage(),0,0);
		PImage playerImg = soni.depthImage();
		playerImg.loadPixels();
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
		
		int[] userIds = soni.getUsers();
		for(int i=0; i<userIds.length; i++){
			if(soni.isTrackingSkeleton(userIds[i])){
				
				User user = userPool.getUser(userIds[i]);
				
				drawHighscore(user);
				
				// get positions and draw
				for(BodyPart part : user.getParts()){
					chooseColor(part);
					get2DPositionAndDraw(part,userIds[i]);
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