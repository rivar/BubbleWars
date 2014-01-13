package com.hackattack.bubblewars.pools.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.hackattack.bubblewars.main.Constants;
import com.hackattack.bubblewars.model.BodyPart;
import com.hackattack.bubblewars.model.ColorSet;
import com.hackattack.bubblewars.model.User;
import com.hackattack.bubblewars.pools.Pool;

public class UserPool implements Pool{

	ArrayList<User> users = new ArrayList<User>();
	ColorSet colorSet;
	
	
	public void addUser(int userId){
		users.add(new User(userId));
	}
	
	public void removeUser(int userId){
		users.remove(getUser(userId));
	}
	
	public void generateColors(int userId){
		User user = getUser(userId);
		Date currentTs = new Date();
		if(currentTs.getTime() - user.getTs().getTime() > Constants.COLOR_CHANGE_INTERVAL){
			user.setTs(currentTs);
			for(BodyPart part : user.getParts()){
				part.setColor(colorSet.getPartColor());
			}
		}
	}
	
	public List<User> getUsers(){
		return users;
	}
	
	public User getUser(int id){
		User out = null;
		for(User user : users){
			if(user.getId() == id){
				out = user;
				break;
			}
		}
		
		return out;
	}
	
	public void verifyColors(){
		// 1. Make ALL Colors IN ColorSet
		// 2. Make sure EACH Color FROM ColorSet is used
		int[] cur_map = new int[Constants.NUM_COLORS];
		List<BodyPart> toChange = new ArrayList<BodyPart>(); 
		for(User user : users){
			for(BodyPart part : user.getParts()){
				if(!colorSet.isInSet(part.getColor())){
					part.setColor(colorSet.getPartColor());
				}
				if(cur_map[part.getColor()] > 0){
					toChange.add(part);
				}
				else{
					cur_map[part.getColor()]++;
				}
			}
			
			for(Integer color : colorSet.getMissingColors(cur_map)){
				if(toChange.size() > 0) toChange.remove(0).setColor(color);
			}
		}
	}
	
	public void setColorSet(ColorSet colorSet){
		this.colorSet = colorSet;
	}
}
