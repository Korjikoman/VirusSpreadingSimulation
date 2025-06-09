package entity;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;
import javax.swing.*;
import javax.swing.Timer;

import java.awt.*;
import javax.imageio.ImageIO;

import main.SPanel;
import tools.Instruments;


public class SimulationObject extends Entity{
  
    private int width;
    private int height;
    public int angle;
    private int number;
    
    
    
    // VIRUS-related SETTINGS
    public boolean infected = false;
    public boolean immune = false;
    private int recoverPercentage = 60;
    
    // RECOVER TIMER 
    public Timer timer;
    public boolean timerStarted = false;
    
    
    SPanel panel;
    
    
    
    
    public double getSpeed() {
        return speed;
    }
    
    public int getObjWidth () {
    	return width;
    }
    
    public void move() {
    	
    	System.out.println("x: " + x + " y: " + y);
        // checking collision
        collisionTop = false;
    	collisionBottom = false;
    	collisionLeft = false;
    	collisionRight = false;
    	// TILE COLLISON
        panel.cChecker.checkTile(this);
        
        // OBJECTS COLLISION
        panel.cChecker.checkObjects(this);
        if (infected) {
        	state = "sick";
        }
        // DEBUG ////////////////////////////////////
        
        	//System.out.println("Collision --> " + collisionTop + " " + collisionBottom + " " + collisionLeft+ " "+ collisionRight);
        
        /////////////////////////////////////////////////
        
        // IF COLLISION -- 
        if (collisionTop || collisionBottom) {
        	angle = 180 - angle;
            
        	//System.out.println(" TOP ");
        }
       
        if (collisionLeft || collisionRight) {
        	angle = -1 * angle;
        	//System.out.println(" LEFT ");
        }
       
        	float dx = (float) ((float) speed * Math.sin((double) Math.toRadians(angle)));;
        	float dy = (float) ((float) speed * Math.cos((double) Math.toRadians(angle)));
        	System.out.println("dx: " + dx + " dy: "+ dy + " speed:" + speed + " angle: " + angle);
        	// set the direction
        	if ( dy < 0 && dx == 0) direction = "up";
        	if ( dy > 0 && dx == 0) direction = "down";
        	if ( dy == 0 && dx < 0) direction = "left";
        	if ( dy == 0 && dx > 0) direction = "right";
        	if ( dy < 0 && dx < 0) direction = "up-left";
        	if ( dy < 0 && dx > 0) direction = "up-right";
        	if ( dy > 0 && dx < 0) direction = "down-left";
        	if ( dy > 0 && dx > 0) direction = "down-right";
        	// update the coordinates
        	x += dx;
            y += dy;
            
            
            
        
        spriteCounter++;
        if (spriteCounter > 10) {
        	if (spriteNum == 1) {
        		spriteNum = 2;
        	}
        	else if (spriteNum == 2) {
        		spriteNum = 1;
        	}
        	spriteCounter = 0;
        }
    }

    public SimulationObject(SPanel sp, float x_pos, float y_pos, int obj_width, int obj_height, int num, boolean is_infected, double objVelocity) {
        x = x_pos;
        y = y_pos;
        width = obj_width;
        height = obj_height;
        number = num;
        infected = is_infected;
        speed = objVelocity;
        
        this.panel = sp;
        
        solidArea = new Rectangle(0,0,width,height);
       
        getObjectImage();
        
        angle = Instruments.random_number(0, 360);
        System.out.println("!!!!x = " + x + " y = " + y + " \n");
        
		
     // FIRST AND LAST INITIALIZATION
	timer = new Timer(2000, new ActionListener() {
	    		
				@Override
				public void actionPerformed(ActionEvent e) {
					int random_num = Instruments.random_number(0, 100);
					if (random_num <= recoverPercentage) {
	    				state = "immune";
	        			infected = false;
	        			immune = true; 
	        			
	        			}
				}
	    	});

        
    }
    
    public int getNum() {
    	return number;
    }
    
    
    public void paintObj(Graphics2D g2) {
    	
        
        BufferedImage image = null;
        
        // animate object action
        switch(state) {
	        case "norm": 
	        	if (spriteNum == 1) {
	        		image = normBro;
	        	}
	        	if (spriteNum == 2) {
	        		image = normBro2;
	        	}
	        	break;
	        
	        case "sick":
	        	if (spriteNum == 1) {
	        		image = sickBro;
	        	}
	        	if (spriteNum == 2) {
	        		image = sickBro2;
	        	}
	        	break;
	        case "immune":
	        	if (spriteNum == 1) {
	        		image = immuneBro;
	        	}
	        	if (spriteNum == 2) {
	        		image = immuneBro2;
	        	}
	        	break;
        }
        
        g2.drawImage(image, (int)x, (int)y, width, height, null);
        
    }
    
    public void getObjectImage() {
    	try {
    		
    		normBro = ImageIO.read(getClass().getResourceAsStream("/player/normBro.png"));
    		normBro2 = ImageIO.read(getClass().getResourceAsStream("/player/normBro2.png"));
    		sickBro = ImageIO.read(getClass().getResourceAsStream("/player/sickBro.png"));
    		sickBro2 = ImageIO.read(getClass().getResourceAsStream("/player/sickBro2.png"));
    		immuneBro = ImageIO.read(getClass().getResourceAsStream("/player/immuneBro.png"));
    		immuneBro2 = ImageIO.read(getClass().getResourceAsStream("/player/immuneBro2.png"));
    		
    	}catch(IOException e) {
    		e.printStackTrace();
    	}
    }
    
   
    
    public void startRecover() {
    	timer.start();
    	timerStarted = true;
    }
    
    public void stopRecover() {
    	timer.stop();
    	timerStarted = false;
    }
    

}

