import processing.core.PApplet;
import processing.core.PVector;
import SimpleOpenNI.SimpleOpenNI;

import java.util.ArrayList;
import java.util.Date;

public class Controller extends PApplet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public SimpleOpenNI soni;
	ArrayList<Bubble> bubbles = new ArrayList<Bubble>();
	ArrayList<User> users = new ArrayList<User>();
	Date startTs;
	Date currentTs;
	Date bubbleSetTs;
	
	
	public void setup(){
		// ts
		startTs= new Date();
		currentTs = startTs;
		bubbleSetTs = startTs;
		
		// cam setup
		soni = new SimpleOpenNI(this);
		soni.enableDepth();
		soni.enableUser();
		size(soni.depthWidth(), soni.depthHeight());
	}
	
	public void onNewUser(SimpleOpenNI context, int userId){
		context.startTrackingSkeleton(userId);
		users.add(new User(userId));
	}
	
	public void onLostUser(SimpleOpenNI context, int userId){
		User user = getUser(userId);
		users.remove(user);
	}
	
	private void determineFillColor(int color){
		if(color == 0) fill(255,0,0,80);
		else if(color == 1) fill(0,255,0,80);
		else if(color == 2) fill(0,0,255,80);
		else if(color == 3) fill(0,255,255,80);
		else fill(0,0,0,0);
	}
	
	private void drawBodyPart(BodyPart part){
		float d = (float) (512e3 / part.getPart3d().z)/2;
		ellipseMode(CENTER);
		determineFillColor(part.getColor());
		ellipse(part.getPart2d().x, part.getPart2d().y, d, d);
	}
	
	private void get2DPositionAndDraw(BodyPart part, int userId){
		
		// get body coords
		soni.getJointPositionSkeleton(userId, part.getPartId(), part.getPart3d());
		soni.convertRealWorldToProjective(part.getPart3d(), part.getPart2d());
		
		drawBodyPart(part);
	}
	
	private void chooseColor(BodyPart part){
		if(currentTs.getTime() - part.getColorTs().getTime() > Constants.START_COLOR_INTERVAL){
			// TODO: Interval [-0.5,3.5] 
			part.setColor((int)Math.round(random(0, Constants.NUM_COLORS-1)));
		}
	}
	
	private void generateBubbles(){
		if(currentTs.getTime() - bubbleSetTs.getTime() > Constants.BUBBLE_SPAWN_INTERVAL){
			int num = (int)(Constants.MAX_BUBBLES - bubbles.size())/2;
			for(int i = 0; i<num; i++){
				// TODO: collision etc...
				
				int size = Math.round(random(Constants.MIN_BUBBLE_SIZE, Constants.MAX_BUBBLE_SIZE));
				Bubble bubble = new Bubble(size);
				
				float x = Math.round(random(0, soni.depthWidth()-bubble.getSize()));
				float y = Math.round(random(0, soni.depthHeight()-bubble.getSize()));
				PVector pos = new PVector(x, y);
				bubble.setPos(pos);
				
				bubbles.add(bubble);
				
				System.out.println("X: "+x+" Y: "+y);
			}
		}
	}
	
	private User getUser(int id){
		User out = null;
		for(User user : users){
			if(user.getId() == id){
				out = user;
				break;
			}
		}
		
		return out;
	}
	
	private boolean isHit(Bubble bubble, BodyPart part){
		// TODO
		return false;
	}
	
	private void onBubbleHit(User user, Bubble bubble){
		// TODO: specific points per bubble?
		user.incrementScore(bubble.getPoints());
	}
	
	private void drawBubble(Bubble bubble){
		ellipseMode(CORNER);
		determineFillColor(bubble.getColor());
		ellipse(bubble.getPos().x, bubble.getPos().y, bubble.getSize(), bubble.getSize());
	}
	
	public void draw(){
		
		// update ts
		currentTs = new Date();
		
		// generate bubbles
		generateBubbles();
		
		// update cam info
		soni.update();
		imageMode(CORNER);
		image(soni.userImage(),0,0);
		
		int[] userIds = soni.getUsers();
		for(int i=0; i<userIds.length; i++){
			if(soni.isTrackingSkeleton(userIds[i])){
				
				User user = getUser(userIds[i]);
				ArrayList<Bubble> hits = new ArrayList<Bubble>();
				
				// get positions and draw
				for(BodyPart part : user.getParts()){
					chooseColor(part);
					get2DPositionAndDraw(part,userIds[i]);
					for(Bubble bubble : bubbles){
						// check for destroyed bubbles
						if(isHit(bubble,part)){
							hits.add(bubble);
							onBubbleHit(user, bubble);
						}
					}
					
					// remove hit bubbles
					for(Bubble bubble : hits){
						bubbles.remove(bubble);
					}
				}
				
				// draw bubbles
				for(Bubble bubble : bubbles){
					bubble.move();
					drawBubble(bubble);
				}
			}
		}
	}
}