package com.hackattack.bubblewars.pools;

import java.util.ArrayList;
import java.util.Date;

import processing.core.PVector;
import SimpleOpenNI.SimpleOpenNI;

import com.hackattack.bubblewars.main.Constants;
import com.hackattack.bubblewars.model.BodyPart;
import com.hackattack.bubblewars.model.Bubble;
import com.hackattack.bubblewars.model.User;
import com.hackattack.bubblewars.util.Util;

public class BubblePool {

	ArrayList<Bubble> bubbles = new ArrayList<Bubble>();
	Date lastTs = new Date();
	final SimpleOpenNI soni;
	
	public BubblePool(SimpleOpenNI soni){
		this.soni=soni;
	}
	
	private boolean isHit(Bubble bubble, BodyPart part){
		// check if x and y coordinates are close enough
		//System.out.println("body part: " + part.getPartId());
		if(bubble.getColor() == part.getColor())
				if(isCloseEnough(bubble.getPos().x, part.getPart2d().x)
						&& isCloseEnough(bubble.getPos().y, part.getPart2d().y)) return true;
//			&& isCloseEnough(bubble.getPos().y, part.getPart2d().y)) System.exit(0);
		return false;
	}
	
	private boolean isCloseEnough(float bubblePos, float partPos){
//		System.out.println("isCloseEnough: abs(" + bubblePos + " - " + partPos + ") = " + abs(bubblePos-partPos));
		if(Math.abs(bubblePos - partPos) < Constants.BODYPART_BUBBLE_MINIMUM_DISTANCE) return true;
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
	
	public void generateBubbles(Date currentTs){
		if(currentTs.getTime() - lastTs.getTime() > Constants.BUBBLE_SPAWN_INTERVAL){
			int num = (int)(Constants.MAX_BUBBLES - bubbles.size())/2;
			for(int i = 0; i<num; i++){
				// TODO: collision etc...
				
				// generate int values of interval [min;max]: 
				int size = Util.random(Constants.MIN_BUBBLE_SIZE, Constants.MAX_BUBBLE_SIZE);
				Bubble bubble = new Bubble(size);
				
				float x = Math.round(Util.random(0, soni.depthWidth()-bubble.getSize()));
				float y = Math.round(Util.random(0, soni.depthHeight()-bubble.getSize()));
				PVector pos = new PVector(x, y);
				bubble.setPos(pos);
				
				bubbles.add(bubble);
				
//				System.out.println("X: "+x+" Y: "+y);
			}
		}
	}
	
	public ArrayList<Bubble> getBubbles(){
		return bubbles;
	}
}