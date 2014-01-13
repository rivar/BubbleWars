package com.hackattack.bubblewars.main;

import processing.core.PVector;

public class Constants {
	
//	public static final int START_BUBBLES_CNT = 1;
	public static final double EXP = 1.01;
	public static final int ADD_COLOR_INTERVAL = 15*1000; // in ms
	public static final int NUM_COLORS = 4;
	public static final int BUBBLE_SPAWN_INTERVAL = 5*1000; // in ms
	public static final int MAX_BUBBLES = 20;
	public static final int MIN_BUBBLE_SIZE = 30;
	public static final int MAX_BUBBLE_SIZE = 70;
	public static final int HIGHSCORE_FONT_SIZE = 32;
	public static final int COLOR_CHANGE_INTERVAL = 15*1000; // in ms
	
	// distances
	public static final PVector BODYPART_BUBBLE_MINIMUM_DISTANCE = new PVector(30, 30);
	public static final PVector BODYPART_TEMPTING_MINIMUM_DISTANCE = new PVector(50, 50);
	
	// modi
	public static final int IN_GAME_MODE = 0;
	public static final int TEMPTING_BUBBLE_MODE = 1;
}
