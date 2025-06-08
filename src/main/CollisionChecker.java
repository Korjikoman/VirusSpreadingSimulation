package main;
import java.awt.Rectangle;
import java.util.ArrayList;

import entity.Entity;
import entity.SimulationObject;
import tools.Instruments;

public class CollisionChecker {
	
	SPanel panel;
	
	public CollisionChecker(SPanel pan){
		this.panel = pan;
	
}	
	
	public void checkTile(Entity entity) {
		// Координаты точек красной зоны
		float entityLeftWorldX = entity.x + entity.solidArea.x; // 
		float entityRightWorldX = entity.x + entity.solidArea.x + entity.solidArea.width;
		float entityTopWorldY = entity.y + entity.solidArea.y;
		float entityBottomWorldY = entity.y + entity.solidArea.y + entity.solidArea.height;
		
		
		// 
		int entityLeftCol = (int)entityLeftWorldX / panel.tileSize; // колонка тайлов до leftX
		int entityRightCol =(int) entityRightWorldX / panel.tileSize; // колонка тайлов до rightX
		int  entityTopRow = (int)entityTopWorldY / panel.tileSize;
		int entityBottomRow = (int)entityBottomWorldY / panel.tileSize;
		
		int tileNum1, tileNum2;
		
		if (entity.direction == "up") {
			entityTopRow = (int)(entityTopWorldY - entity.speed)/panel.tileSize;
			tileNum1 = panel.tileM.mapTileNum[entityLeftCol][entityTopRow];
			tileNum2 = panel.tileM.mapTileNum[entityRightCol][entityTopRow];
			
			if (panel.tileM.tile[tileNum1].collision == true || panel.tileM.tile[tileNum2].collision == true)
			{
				entity.collisionTop = true;
			}
			
		}
		else if (entity.direction == "down") {
			entityBottomRow = (int)(entityBottomWorldY + entity.speed)/panel.tileSize;
			
			if (entityBottomRow >= 12) {
				entity.collisionBottom= true;
				return;
			}
			
			tileNum1 = panel.tileM.mapTileNum[entityLeftCol][entityBottomRow];
			if (entityRightCol >= 16) {
				entity.collisionRight= true;
				return;
			}
			tileNum2 = panel.tileM.mapTileNum[entityRightCol][entityBottomRow];
			//System.out.println("DOWN tile1 - " + tileNum1 + " tile2 -> " + tileNum2);
			if (panel.tileM.tile[tileNum1].collision == true || panel.tileM.tile[tileNum2].collision == true)
			{
				entity.collisionBottom = true;
			}
			
		}
		else if (entity.direction == "left") {
			entityLeftCol = (int)(entityLeftWorldX - entity.speed)/panel.tileSize;
			
			tileNum1 = panel.tileM.mapTileNum[entityLeftCol][entityTopRow];
			tileNum2 = panel.tileM.mapTileNum[entityLeftCol][entityBottomRow];
			
			if (panel.tileM.tile[tileNum1].collision == true || panel.tileM.tile[tileNum2].collision == true)
			{
				entity.collisionLeft = true;
			}
		}
		else if (entity.direction == "right") {
			entityRightCol = (int)(entityRightWorldX + entity.speed)/panel.tileSize;
			if (entityRightCol >= 16) {
				entity.collisionRight= true;
				return;
			}
			
			
			tileNum1 = panel.tileM.mapTileNum[entityRightCol][entityTopRow];
			tileNum2 = panel.tileM.mapTileNum[entityRightCol][entityBottomRow];
			//System.out.println("RIGHT tile1 - " + tileNum1 + " tile2 -> " + tileNum2);
			if (panel.tileM.tile[tileNum1].collision == true || panel.tileM.tile[tileNum2].collision == true)
			{
				entity.collisionRight = true;
			}
		}
		else if (entity.direction == "up-left") {
			entityTopRow = (int)(entityTopWorldY - entity.speed)/panel.tileSize;
			
			if (entityTopRow >= 16) {
				entity.collisionTop= true;
				return;
			}
			
			tileNum1 = panel.tileM.mapTileNum[entityLeftCol][entityTopRow];
			entityTopRow = (int)entityTopWorldY / panel.tileSize;
			
			entityLeftCol = (int)(entityLeftWorldX - entity.speed)/panel.tileSize;			
			tileNum2 = panel.tileM.mapTileNum[entityLeftCol][entityTopRow];
			
			if (panel.tileM.tile[tileNum1].collision == true)
			{
				entity.collisionTop = true;
				
			}
			
			else if ( panel.tileM.tile[tileNum2].collision == true) {
				entity.collisionLeft = true;
			}
			
		}
		else if (entity.direction == "up-right") {
			
			if (entityRightCol >= 16) {
				entity.collisionRight= true;
				return;
			}
			
			entityTopRow = (int)(entityTopWorldY - entity.speed)/panel.tileSize;
			tileNum1 = panel.tileM.mapTileNum[entityRightCol][entityTopRow];
			entityTopRow = (int)entityTopWorldY / panel.tileSize;
			
			entityRightCol = (int)(entityRightWorldX + entity.speed)/panel.tileSize;
			if (entityRightCol >= 16) {
				entity.collisionRight= true;
				return;
			}
			
			tileNum2 = panel.tileM.mapTileNum[entityRightCol][entityTopRow];
			//System.out.println("UP-RIGHT tile1 - " + tileNum1 + " tile2 -> " + tileNum2);
			if (panel.tileM.tile[tileNum1].collision == true)
			{
				entity.collisionTop = true;
				
			}
			
			else if ( panel.tileM.tile[tileNum2].collision == true) {
				entity.collisionRight = true;
			}
			
		}
		else if (entity.direction == "down-left") {
			entityBottomRow = (int)(entityBottomWorldY + entity.speed)/panel.tileSize;
			if (entityBottomRow >= 12) {
				entity.collisionBottom= true;
				return;
			}
			tileNum1 = panel.tileM.mapTileNum[entityLeftCol][entityBottomRow];
			entityBottomRow = (int)entityBottomWorldY / panel.tileSize;
			
			entityLeftCol = (int)(entityLeftWorldX - entity.speed)/panel.tileSize;
			tileNum2 = panel.tileM.mapTileNum[entityLeftCol][entityBottomRow];
			//System.out.println("DOWN-LEFT tile1 - " + tileNum1 + " tile2 -> " + tileNum2);
			if (panel.tileM.tile[tileNum1].collision == true)
			{
				entity.collisionBottom = true;
				
			}
			
			else if ( panel.tileM.tile[tileNum2].collision == true) {
				entity.collisionLeft = true;
			}
		}
		else if (entity.direction == "down-right") {
			
			
			
			//System.out.println("DOWN-RIGHTTTT");
			entityBottomRow = (int)(entityBottomWorldY + entity.speed)/panel.tileSize;
			if (entityBottomRow >= 12) {
				entity.collisionBottom= true;
				return;
			}
			
			if (entityRightCol >= 16) {
				entity.collisionRight= true;
				return;
			}
			
			tileNum1 = panel.tileM.mapTileNum[entityRightCol][entityBottomRow];
			entityBottomRow = (int)entityBottomWorldY / panel.tileSize;
			
			entityRightCol = (int)(entityRightWorldX + entity.speed)/panel.tileSize;
			if (entityRightCol >= 16) {
				entity.collisionRight= true;
				return;
			}
			
			//System.out.println("RIGHT-col = " + entityRightCol);
			entityRightCol = (int)(entityRightWorldX + entity.speed)/panel.tileSize;
			
			
			tileNum2 = panel.tileM.mapTileNum[entityRightCol][entityBottomRow];
			//System.out.println("DOWN-RIGHT tile1 - " + tileNum1 + " tile2 -> " + tileNum2);
			if (panel.tileM.tile[tileNum1].collision == true)
			{
				entity.collisionBottom = true;
				
			}
			
			else if ( panel.tileM.tile[tileNum2].collision == true) {
				entity.collisionRight = true;
			}
		}
		
	}



	public void checkObjects(SimulationObject simulationObject) {
	    ArrayList<SimulationObject> objects = panel.getObjects();

	    Rectangle objBounds = new Rectangle(
	        (int)(simulationObject.x + simulationObject.solidArea.x),
	        (int)(simulationObject.y + simulationObject.solidArea.y),
	        simulationObject.solidArea.width,
	        simulationObject.solidArea.height
	    );

	    for (SimulationObject other : objects) {
	        if (other == simulationObject || other == null) continue;

	        Rectangle otherBounds = new Rectangle(
	            (int)(other.x + other.solidArea.x),
	            (int)(other.y + other.solidArea.y),
	            other.solidArea.width,
	            other.solidArea.height
	        );

	        if (objBounds.intersects(otherBounds)) {
	        	// Реакция: изменить угол движения (отскок)
	            float dx = simulationObject.x - other.x;
	            float dy = simulationObject.y - other.y;
	            double angle = Math.toDegrees(Math.atan2(dy, dx));

	            // Изменить угол на противоположный
	            simulationObject.angle = (int)(angle + Instruments.random_number(-45, 45)) % 360;
	            if (simulationObject.angle < 0) simulationObject.angle += 360;

	            // Немного сдвинуть объект, чтобы избежать залипания
	            simulationObject.x += Math.cos(Math.toRadians(simulationObject.angle)) * simulationObject.speed;
	            simulationObject.y += Math.sin(Math.toRadians(simulationObject.angle)) * simulationObject.speed;

	            //System.out.println("INTERSECTS! " + simulationObject.getNum() + " <-> " + other.getNum());
	        	
	            if (simulationObject.infected && !(other.immune)) {
	            	other.infected = true;
	            }
	        	
	        }
	    }
	}
	
}