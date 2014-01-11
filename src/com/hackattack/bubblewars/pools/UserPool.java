package com.hackattack.bubblewars.pools;

import com.hackattack.bubblewars.model.User;
import java.util.ArrayList;;

public class UserPool {

	ArrayList<User> users = new ArrayList<User>();

	public void addUser(int userId){
		users.add(new User(userId));
	}
	
	public void removeUser(int userId){
		users.remove(getUser(userId));
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
}
