package entity;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

public class Entity {
	public double speed;
	public double x, y;
	
	
	
	public BufferedImage normBro, normBro2, sickBro, sickBro2, immuneBro, immuneBro2,reinfectedBro, reinfectedBro2, deadBro, deadBro2;
	public String state = "norm";
	
	public String direction = "down";
	
	public int spriteCounter = 0;
	public int spriteNum = 1;
	
	
	public Rectangle solidArea; // collision area
	public boolean collisionTop = false;
	public boolean collisionBottom = false;
	public boolean collisionLeft = false;
	public boolean collisionRight = false;
	
}
