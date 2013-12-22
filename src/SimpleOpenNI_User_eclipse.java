import SimpleOpenNI.*;
import processing.core.*;

public class SimpleOpenNI_User_eclipse extends PApplet {

	public SimpleOpenNI soni;
	public PImage face;
	
	public void setup(){
		soni = new SimpleOpenNI(this);
		soni.enableDepth();
		soni.enableUser();
		size(soni.depthWidth(), soni.depthHeight());
		
	    String url = "http://boards.dirtypotter.com/src/1359339488615.png";
	    
	    // Load image from a web server
	    face = loadImage(url, "png");
		
		textSize(20);
		fill(0,255,0);
	}
	
	public void onNewUser(SimpleOpenNI context, int userId){
		context.startTrackingSkeleton(userId);
	}
	
	public void draw(){
		soni.update();
		imageMode(CORNER);
		image(soni.userImage(),0,0);
		
		int[] userIds = soni.getUsers();
		for(int i=0; i<userIds.length; i++){
			if(soni.isTrackingSkeleton(userIds[i])){
				PVector head3d = new PVector();
				PVector head2d = new PVector();
				
				float confidence = soni.getJointPositionSkeleton(userIds[i], SimpleOpenNI.SKEL_LEFT_HAND, head3d);
				soni.convertRealWorldToProjective(head3d, head2d);
				float d = (float) (512e3 / head3d.z);
				
				imageMode(CENTER);
				image(face,head2d.x, head2d.y, d, d);
				
				text("head3d: " + head3d, 10, 20);
				text("head2d: " + head2d, 10, 40);
			}
		}
	}
	
	
	
	

//	public void setup()
//	{
//	 // context = new SimpleOpenNI(this);
//	  context = new SimpleOpenNI(this,SimpleOpenNI.RUN_MODE_MULTI_THREADED);
//	  
//	  // enable depthMap generation 
//	  context.enableDepth();
//	  
//	  // enable skeleton generation for all joints
//	  context.enableUser(SimpleOpenNI.SKEL_HEAD);
//	 
//	  background(200,0,0);
//
//	  stroke(0,0,255);
//	  strokeWeight(3);
//	  smooth();
//	  
//	  size(context.depthWidth(), context.depthHeight()); 
//	}
//
//	public void draw()
//	{
//	  // update the cam
//	  context.update();
//	  
//	  // draw depthImageMap
//	  image(context.depthImage(),0,0);
//	  
//	  // draw the skeleton if it's available
//	  if(context.isTrackingSkeleton(1))
//	    drawSkeleton(1);
//	}
//
//	// draw the skeleton with the selected joints
//	public void drawSkeleton(int userId)
//	{
//	  // to get the 3d joint data
//	  /*
//	  PVector jointPos = new PVector();
//	  context.getJointPositionSkeleton(userId,SimpleOpenNI.SKEL_NECK,jointPos);
//	  println(jointPos);
//	  */
//	  
//	  context.drawLimb(userId, SimpleOpenNI.SKEL_HEAD, SimpleOpenNI.SKEL_NECK);
//
//	  context.drawLimb(userId, SimpleOpenNI.SKEL_NECK, SimpleOpenNI.SKEL_LEFT_SHOULDER);
//	  context.drawLimb(userId, SimpleOpenNI.SKEL_LEFT_SHOULDER, SimpleOpenNI.SKEL_LEFT_ELBOW);
//	  context.drawLimb(userId, SimpleOpenNI.SKEL_LEFT_ELBOW, SimpleOpenNI.SKEL_LEFT_HAND);
//
//	  context.drawLimb(userId, SimpleOpenNI.SKEL_NECK, SimpleOpenNI.SKEL_RIGHT_SHOULDER);
//	  context.drawLimb(userId, SimpleOpenNI.SKEL_RIGHT_SHOULDER, SimpleOpenNI.SKEL_RIGHT_ELBOW);
//	  context.drawLimb(userId, SimpleOpenNI.SKEL_RIGHT_ELBOW, SimpleOpenNI.SKEL_RIGHT_HAND);
//
//	  context.drawLimb(userId, SimpleOpenNI.SKEL_LEFT_SHOULDER, SimpleOpenNI.SKEL_TORSO);
//	  context.drawLimb(userId, SimpleOpenNI.SKEL_RIGHT_SHOULDER, SimpleOpenNI.SKEL_TORSO);
//
//	  context.drawLimb(userId, SimpleOpenNI.SKEL_TORSO, SimpleOpenNI.SKEL_LEFT_HIP);
//	  context.drawLimb(userId, SimpleOpenNI.SKEL_LEFT_HIP, SimpleOpenNI.SKEL_LEFT_KNEE);
//	  context.drawLimb(userId, SimpleOpenNI.SKEL_LEFT_KNEE, SimpleOpenNI.SKEL_LEFT_FOOT);
//
//	  context.drawLimb(userId, SimpleOpenNI.SKEL_TORSO, SimpleOpenNI.SKEL_RIGHT_HIP);
//	  context.drawLimb(userId, SimpleOpenNI.SKEL_RIGHT_HIP, SimpleOpenNI.SKEL_RIGHT_KNEE);
//	  context.drawLimb(userId, SimpleOpenNI.SKEL_RIGHT_KNEE, SimpleOpenNI.SKEL_RIGHT_FOOT);  
//	}
//
//	// -----------------------------------------------------------------
//	// SimpleOpenNI events
//
//	public void onNewUser(int userId)
//	{
//	  println("onNewUser - userId: " + userId);
//	  println("  start pose detection");
//	  
//	  context.startTrackingSkeleton(userId);
////	  context.startPoseDetection("Psi",userId);
//	}
//
//	public void onLostUser(int userId)
//	{
//	  println("onLostUser - userId: " + userId);
//	}
//
//	public void onStartCalibration(int userId)
//	{
//	  println("onStartCalibration - userId: " + userId);
//	}
//
////	public void onEndCalibration(int userId, boolean successfull)
////	{
////	  println("onEndCalibration - userId: " + userId + ", successfull: " + successfull);
////	  
////	  if (successfull) 
////	  { 
////	    println("  User calibrated !!!");
////	    context.startTrackingSkeleton(userId); 
////	  } 
////	  else 
////	  { 
////	    println("  Failed to calibrate user !!!");
////	    println("  Start pose detection");
////	    context.startPoseDetection("Psi",userId);
////	  }
////	}
////
////	public void onStartPose(String pose,int userId)
////	{
////	  println("onStartPose - userId: " + userId + ", pose: " + pose);
////	  println(" stop pose detection");
////	  
////	  context.stopPoseDetection(userId); 
////	  context.requestCalibrationSkeleton(userId, true);
////	 
////	}
//
//	public void onEndPose(String pose,int userId)
//	{
//	  println("onEndPose - userId: " + userId + ", pose: " + pose);
//	}
}
