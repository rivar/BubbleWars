package com.hackattack.bubblewars.main;

import processing.core.PApplet;

public class Launcher 
{
	public static void main(String args[])
	{
		// --present ????
		PApplet.main(new String[] {"--location=0,0", "--present", "com.hackattack.bubblewars.main.Applet"});
	}
}