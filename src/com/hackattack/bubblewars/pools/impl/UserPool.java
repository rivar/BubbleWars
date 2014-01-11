package com.hackattack.bubblewars.pools.impl;

import java.util.Date;
import java.util.ArrayList;

import com.hackattack.bubblewars.main.Constants;
import com.hackattack.bubblewars.model.BodyPart;
import com.hackattack.bubblewars.model.ColorSet;
import com.hackattack.bubblewars.model.User;
import com.hackattack.bubblewars.pools.Pool;

public class UserPool implements Pool{

	ArrayList<User> users = new ArrayList<User>();
	final ColorSet colorSet;
	
	public UserPool(ColorSet colorSet){
		this.colorSet = colorSet;
	}
	
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
		for(User user : users){
			for(BodyPart part : user.getParts()){
				if(!colorSet.isInSet(part.getColor())){
					part.setColor(colorSet.getPartColor());
				}
			}
		}
	}
}
