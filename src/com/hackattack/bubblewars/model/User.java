package com.hackattack.bubblewars.model;
import java.util.ArrayList;
import java.util.Date;

import SimpleOpenNI.SimpleOpenNI;


public class User {

	int score = 0;
	int id;
	ArrayList<BodyPart> parts = new ArrayList<BodyPart>();
	Date ts;
	
	public User(int id){
		this.id = id;
		ts = new Date();
		parts.add(new BodyPart(SimpleOpenNI.SKEL_LEFT_HAND));
		parts.add(new BodyPart(SimpleOpenNI.SKEL_RIGHT_HAND));
		parts.add(new BodyPart(SimpleOpenNI.SKEL_LEFT_FOOT));
		parts.add(new BodyPart(SimpleOpenNI.SKEL_RIGHT_FOOT));
	}
	
	public void setTs(Date ts){
		this.ts = ts;
	}
	
	public Date getTs(){
		return ts;
	}

	public int getId() {
		return id;
	}

	public ArrayList<BodyPart> getParts() {
		return parts;
	}
	
	public void incrementScore(int points){
		score += points;
	}
	
	public int getScore(){
		return score;
	}
}