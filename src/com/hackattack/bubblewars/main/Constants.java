package com.hackattack.bubblewars.main;

import processing.core.PVector;

public class Constants {
	
//	public static final int START_BUBBLES_CNT = 1;
	public static final double EXP = 1.01;
	public static final int ADD_COLOR_INTERVAL = 15*1000; // in ms
	public static final int NUM_COLORS = 4;
	public static final int BUBBLE_SPAWN_INTERVAL = 5*1000; // in ms
	public static final int MAX_BUBBLES = 20;
	public static int MIN_BUBBLE_SIZE = 60;
	public static int MAX_BUBBLE_SIZE = 140;
	public static final int HIGHSCORE_FONT_SIZE = 32;
	public static final int COLOR_CHANGE_INTERVAL = 15*1000; // in ms
	public static int TEMPTING_BUBBLE_INITIAL_SIZE = 40;
	public static final int TEMPTING_BUBBLE_MAX_GAIN = 250;
	public static int MENU_SIZE = 70;
	public static final int MENU_CHOICE_TIME = 3; // in s
	public static final int COUNTER_TIME = 20; // in s
	public static final int ALLOWED_USERS = 2;
	public static int TEMPTING_BUBBLE_NEEDED_SIZE = 50;
	public static final int MAX_DELTA_X = 20;
	public static final int MAX_DELTA_Y = 20;
	public static final int BUBBLE_HIT_ANIMATION_TIME = 3000; // in ms
	public static final int BUBBLE_SPLASH_FONT_SIZE = 16;
	public static final int TEMPTING_BUBBLE_SMASH_TIME = 3; // in s
	public static final int HIGHSCORE_COUNTDOWN = 10; // in s
	public static final int HIGHSCORE_PIC_SHOW = 5; // in s
	
	// distances
	public static PVector BODYPART_BUBBLE_MINIMUM_DISTANCE = new PVector(30, 30);
	public static PVector BODYPART_TEMPTING_MINIMUM_DISTANCE = new PVector(50, 50);
	public static final int OPTIMAL_USER_DISTANCE = 2250;
	
	// modi
	public static final int IN_GAME_MODE = 0;
	public static final int TEMPTING_BUBBLE_MODE = 1;
	public static final int MENU_MODE = 2;
	public static final int HIGHSCORE_MODE = 3;
	
	public static int WIDTH = 1024;//640;
	public static int HEIGHT = 768;//480;
}