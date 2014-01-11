package com.hackattack.bubblewars.util;


public class Util {

	public static int random(int min, int max){
		return min+(int)(Math.random()*((max-min)+1));
	}
	
	public static double random(double min, double max){
		return min+(Math.random()*((max-min)+1));
	}
	
	public static float random(float min, float max){
		return min+(float)(Math.random()*((max-min)+1));
	}
	
	public static float[] decodePartFillColor(int color){
		if(color == 0) return new float[]{255,0,0,180};
		else if(color == 1) return new float[]{0,255,0,180};
		else if(color == 2) return new float[]{0,0,255,180};
		else if(color == 3) return new float[]{255,0,255,180};
		else return new float[]{0,0,0,0};
	}
	
	public static float[] decodeBubbleFillColor(int color){
		if(color == 0) return new float[]{255,0,0,80};
		else if(color == 1) return new float[]{0,255,0,80};
		else if(color == 2) return new float[]{0,0,255,80};
		else if(color == 3) return new float[]{255,0,255,80};
		else return new float[]{0,0,0,0};
	}
}
