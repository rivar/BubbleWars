package com.hackattack.bubblewars.model;

import java.util.Vector;

import com.hackattack.bubblewars.main.Constants;
import com.hackattack.bubblewars.util.Util;

public class ColorSet {

	int[] map;
	Vector<Integer> set;
	
	int _idx = 0;
	
	public ColorSet(Vector<Integer> set){
		this.set = set;
		map = new int[Constants.NUM_COLORS];
		for(int i=0; i<this.set.size(); i++){
			map[set.get(i)] = 1;
		}
	}
	
	public boolean isInSet(int color){
		return (map[color] == 1);
	}
	
	public int getBubbleColor(){
		return set.get(Util.random(0, set.size()-1));
	}
	
	public int getPartColor(){
		if(set.size() == 0) return 0;
		
		if(_idx == set.size()-1){
			_idx = 0;
			return set.get(_idx);
		} else{
			return set.get(++_idx);
		}
	}
	
	public void addColor(){
		
		if(map.length <= set.size()) return;
		
		int _cur = Util.random(0, map.length-1);
		int iter = 0;
		while(map[_cur] == 1 && iter <= map.length){
			if(_cur == map.length-1){
				_cur = 0;
			} else{
				_cur++;
			}
			
			iter++;
		}
		
		if(map[_cur] == 0){
			if(addColor(_cur)){
				map[_cur] = 1;
			}
		}
	}
	
	private boolean addColor(int c){
		set.add(c);
		return true;
	}
}
