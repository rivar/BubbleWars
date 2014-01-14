package com.hackattack.bubblewars.pools.impl;

import java.util.ArrayList;
import java.util.Date;

import processing.core.PVector;
import SimpleOpenNI.SimpleOpenNI;

import com.hackattack.bubblewars.main.Constants;
import com.hackattack.bubblewars.model.BodyPart;
import com.hackattack.bubblewars.model.Bubble;
import com.hackattack.bubblewars.model.ColorSet;
import com.hackattack.bubblewars.model.User;
import com.hackattack.bubblewars.pools.Pool;
import com.hackattack.bubblewars.util.Util;

public class BubblePool implements Pool{

	ArrayList<Bubble> bubbles = new ArrayList<Bubble>();
	Date lastTs = new Date();
	final ColorSet colorSet;
	final SimpleOpenNI soni;
	
	public BubblePool(SimpleOpenNI soni, ColorSet colorSet){
		this.soni=soni;
		this.colorSet = colorSet;
	}
	
	private boolean isHit(Bubble bubble, BodyPart part){
		// check if x and y coordinates are close enough
		if(bubble.getColor() == part.getColor())
				if(Util.isCloseEnough(bubble.getPos(), part.getPart2d(), Constants.BODYPART_BUBBLE_MINIMUM_DISTANCE)) return true;
		return false;
	}
	
	public void checkHits(BodyPart part, User user){
		ArrayList<Bubble> hits = new ArrayList<Bubble>();
		for(Bubble bubble : bubbles){
			// check for destroyed bubbles
			if(isHit(bubble,part)){
				hits.add(bubble);
				onBubbleHit(user, bubble);
			}
		}
		
		// remove hit bubbles
		for(Bubble bubble : hits){
			bubbles.remove(bubble);
		}
	}
	
	private void onBubbleHit(User user, Bubble bubble){
		// TODO: specific points per bubble?
		user.incrementScore(bubble.getPoints());
	}
	
	private void generateColor(Bubble bubble){
		bubble.setColor(colorSet.getBubbleColor());
	}
	
	public void verifyColors(){
		// 1. Make ALL Colors IN ColorSet
		for(Bubble bubble : bubbles){
			if(!colorSet.isInSet(bubble.getColor())){
				bubble.setColor(colorSet.getBubbleColor());
			}
		}
	}
	
	private void generatePosition(Bubble bubble){
		float x = Math.round(Util.random(0, soni.depthWidth()-bubble.getSize()));
		float y = Math.round(Util.random(0, soni.depthHeight()-bubble.getSize()));
		PVector pos = new PVector(x, y);
		bubble.setPos(pos);
	}
	
	public void generateBubbles(){
		Date currentTs = new Date();
		if(currentTs.getTime() - lastTs.getTime() > Constants.BUBBLE_SPAWN_INTERVAL){
			int num = (int)(Constants.MAX_BUBBLES - bubbles.size())/2;
			for(int i = 0; i<num; i++){
				// TODO: collision etc...
				
				// generate int values of interval [min;max]: 
				int size = Util.random(Constants.MIN_BUBBLE_SIZE, Constants.MAX_BUBBLE_SIZE);
				Bubble bubble = new Bubble(size,Constants.MAX_BUBBLE_SIZE-size);
				generatePosition(bubble);
				generateColor(bubble);
				
				bubbles.add(bubble);
				
//				System.out.println("X: "+x+" Y: "+y);
			}
		}
	}
	
	public ArrayList<Bubble> getBubbles(){
		return bubbles;
	}
	
	public void setTs(Date lastTs){
		this.lastTs = lastTs;
	}
}