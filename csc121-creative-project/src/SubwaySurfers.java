import java.util.ArrayList;

import processing.core.PApplet;
import processing.event.KeyEvent;


/**
 * Represents an interactive application where a drop of
 * water falls down from the top of the window. If the 
 * user clicks the mouse, the drop is moved over to the
 * location of the click;
 */
public class SubwaySurfers {
	Player p;
	
	Ground g;
	
	ArrayList<Train3D> trains;
	
	ArrayList<Obstacle> obstacles;
	
	float gameSpd = SSConstants.gameSpd;  // controls the speed of the game

	/* 
	 * Create new game with player at given x, y and train on the left track
	 */
    public SubwaySurfers() {
    	this.p = new Player();
    	this.trains = new ArrayList<Train3D>();
    	this.obstacles = new ArrayList<Obstacle>();
    	this.g = new Ground();
    }
    
    /*
     * Create new object with given player and train list
     */
    public SubwaySurfers(Player p, ArrayList<Train3D> t, Ground g, ArrayList<Obstacle> o) {
    	this.p = p;
    	this.trains = t;
    	this.g = g;
    	this.obstacles = o;
    }
    
    /**
     * Renders a picture of the player and obstacles on the window
     */
    public PApplet draw(PApplet c) {
    	// colors the canvas background
        c.background(45, 160, 230);
        trains.forEach(train -> train.draw(c));
        obstacles.forEach(obstacle -> obstacle.draw(c));
        p.draw(c);
        // positions the camera at (x1,y1,z1) looking toward (x2,y2,z2) SSConstants.HEIGHT/2 + (SSConstants.HEIGHT/2 - p.pos.y)/2
        c.camera(p.pos.x, SSConstants.HEIGHT/2, SSConstants.CAMERA_Z, p.pos.x, SSConstants.HEIGHT, 0, 0, 1, 0);
        g.draw(c);
        
        return c;
    }

    /**
     * Produces an updated world where the player and obstacles move if needed
     */
    public SubwaySurfers update() {
        p.update();
        
        trains.removeIf(train -> (train.pos.z - train.length) >= SSConstants.DELETE_POINT);  // removes trains that are off the screen
        trains.forEach(train -> train.update());
        
        obstacles.removeIf(obstacle -> obstacle.offScreen);  // removes trains that are off the screen
        obstacles.forEach(obstacle -> obstacle.update());
        
        if (collision()) {
        	System.out.println("bruh!!!");
        }
        
        return new SubwaySurfers(p, trains, g, obstacles);
        
    }
    
    public SubwaySurfers keyPressed(KeyEvent kev) {
    	p.move(kev);
    	
    		if (kev.getKey() == '1') {
    			trains.add( new Train3D(2000, 1, 10, false));
    		} else if (kev.getKey() == '2') {
    			trains.add( new Train3D(600, 2, 10, false));
    		} else if (kev.getKey() == '3') {
    			trains.add( new Train3D(700, 3, 25, false));
    		} else if (kev.getKey() == '4') {
    			obstacles.add(new Obstacle(1));
    		} else if (kev.getKey() == '5') {
    			obstacles.add(new Obstacle(2));
    		} else if (kev.getKey() == '6') {
    			obstacles.add(new Obstacle(3));
    		}
    	
    	
    	return new SubwaySurfers(p, trains, g, obstacles);
    }
    
   boolean collision() {
	   for (int t = 0; t < trains.size(); t++) {
		   return (trains.get(t).frontZ >= p.pos.z && trains.get(t).rearZ <= p.pos.z && trains.get(t).track == p.currentTrack);
	   }
	   
	   return false;
   }
}
