package main;
import java.awt.Rectangle;
import java.util.ArrayList;

import entity.Entity;
import entity.SimulationObject;
import tools.Instruments;

public class CollisionChecker {
	
	SPanel panel;
	
	int maxRow;
	int maxCol;
	
	
	public CollisionChecker(SPanel pan){
		this.panel = pan;
		this.maxCol = pan.maxScreenCol;
		this.maxRow = pan.maxScreenRow;
	
}	
	// COLLISION BETWEEEN OBJECT AND TILES
	public boolean checkTile(Entity entity) {
		// Координаты точек красной зоны
		float entityLeftWorldX = entity.x + entity.solidArea.x; // 
		float entityRightWorldX = entity.x + entity.solidArea.x + entity.solidArea.width;
		float entityTopWorldY = entity.y + entity.solidArea.y;
		float entityBottomWorldY = entity.y + entity.solidArea.y + entity.solidArea.height;
		System.out.println(" Lx"+entityLeftWorldX + "\n Rx"+ entityRightWorldX + "\n Ty"+ entityTopWorldY + "\n By"+ entityBottomWorldY + "\n ");
		
		// 
		int entityLeftCol = (int)entityLeftWorldX / panel.tileSize; // колонка тайлов до leftX
		int entityRightCol =(int) entityRightWorldX / panel.tileSize; // колонка тайлов до rightX
		int entityTopRow = (int)entityTopWorldY / panel.tileSize;
		int entityBottomRow = (int)entityBottomWorldY / panel.tileSize;
		System.out.println("L"+entityLeftCol + " R"+ entityRightCol + " T"+ entityTopRow + " B"+ entityBottomRow + " ");
		System.out.println("Direction: " + entity.direction);
		
		int tileNum1, tileNum2;
		// EXTRA TILES //////////////////
		int extraTile1;
		int extraTile2;
		int extraTile3;
		int extraTile4;
		int extraTile5;
		int extraTile6;
		int extraTile7;
		int extraTile8;
		////////////////////////
		
		int flag = 0;
		
		// TOP ///////////////////////////////////
		entityTopRow = (int)(entityTopWorldY - entity.speed)/panel.tileSize;
		if (entityTopRow < 0) {entity.collisionTop = true;
		flag = 1;}
		else {
			tileNum1 = panel.tileM.mapTileNum[entityLeftCol][entityTopRow];
			
			
			tileNum2 = panel.tileM.mapTileNum[entityRightCol][entityTopRow];
			
			if (panel.tileM.tile[tileNum1].collision == true || panel.tileM.tile[tileNum2].collision == true)
			{
				entity.collisionTop = true;
				flag = 1;
				
			}
		}
		
		
		entityTopRow = (int)entityTopWorldY / panel.tileSize; // default value
		///////////////////////////////////////////

		// BOTTOM ///////////////////////////////////	
		entityBottomRow = (int)(entityBottomWorldY + entity.speed)/panel.tileSize;
		if (entityBottomRow >= panel.maxScreenRow) {
			entity.collisionBottom = true;
			flag = 1;
			
		}
		else {
			tileNum1 = panel.tileM.mapTileNum[entityLeftCol][entityBottomRow];
			tileNum2 = panel.tileM.mapTileNum[entityRightCol][entityBottomRow];
			//System.out.println("DOWN tile1 - " + tileNum1 + " tile2 -> " + tileNum2);
			if (panel.tileM.tile[tileNum1].collision == true || panel.tileM.tile[tileNum2].collision == true)
			{
				entity.collisionBottom = true;
				flag = 1;
				
			}
		}
		
		entityBottomRow = (int)entityBottomWorldY / panel.tileSize; // default value
		///////////////////////////////////////////
		
		// LEFT ///////////////////////////////////
		entityLeftCol = (int)(entityLeftWorldX - entity.speed)/panel.tileSize;
		if (entityLeftCol < 0) {
			entity.collisionLeft = true;
			flag = 1;
		}
		else {
			tileNum1 = panel.tileM.mapTileNum[entityLeftCol][entityTopRow];
			tileNum2 = panel.tileM.mapTileNum[entityLeftCol][entityBottomRow];
			
			if (panel.tileM.tile[tileNum1].collision == true || panel.tileM.tile[tileNum2].collision == true)
			{
				entity.collisionLeft = true;
				flag = 1;
				
			}
		}
		
		entityLeftCol = (int)entityLeftWorldX / panel.tileSize; // default value
		///////////////////////////////////////////
		
		// RIGHT ///////////////////////////////////
		entityRightCol = (int)(entityRightWorldX + entity.speed)/panel.tileSize;
		if (entityRightCol >= panel.maxScreenCol) {
			entity.collisionRight = true;
			flag = 1;
		}
		else {
			tileNum1 = panel.tileM.mapTileNum[entityRightCol][entityTopRow];
			tileNum2 = panel.tileM.mapTileNum[entityRightCol][entityBottomRow];
			//System.out.println("RIGHT tile1 - " + tileNum1 + " tile2 -> " + tileNum2);
			if (panel.tileM.tile[tileNum1].collision == true || panel.tileM.tile[tileNum2].collision == true)
			{
				entity.collisionRight = true;
				flag = 1;
			}
		}
		
		entityRightCol =(int) entityRightWorldX / panel.tileSize; // default value
		
		///////////////////////////////////////////
		if (flag == 1) {
			return true;
		}
		return false;
		
		
	}


	// COLLISION BETWEEEN OBJECTS
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
	           
	        	System.out.println("COLLISION BETWEEN OBJECTS");
	        	
	        	//HANDLE OBJECT BEHAVIOR AFTER COLLISION
	        	resolveCollision(simulationObject, other);
	            	        	
	            if (simulationObject.infected && !(other.immune)) {
	            	other.infected = true;
	            }
	        	
	        }
	    }
	}
	private void resolveCollision(SimulationObject obj1, SimulationObject obj2) {
		// Вычисляем векторы нормали
	    float dx = (obj2.x + obj2.solidArea.x + obj2.solidArea.width/2f) - 
	               (obj1.x + obj1.solidArea.x + obj1.solidArea.width/2f);
	    float dy = (obj2.y + obj2.solidArea.y + obj2.solidArea.height/2f) - 
	               (obj1.y + obj1.solidArea.y + obj1.solidArea.height/2f);
	    float distance = (float) Math.sqrt(dx*dx + dy*dy);
	    
	    if (distance == 0) return; // Защита от деления на ноль
	    
	    dx /= distance;
	    dy /= distance;

	    // Вычисляем относительную скорость
	    float v1x = (float) (obj1.speed * Math.sin(Math.toRadians(obj1.angle)));
	    float v1y = (float) (obj1.speed * Math.cos(Math.toRadians(obj1.angle)));
	    float v2x = (float) (obj2.speed * Math.sin(Math.toRadians(obj2.angle)));
	    float v2y = (float) (obj2.speed * Math.cos(Math.toRadians(obj2.angle)));
	    
	    float dotProduct1 = v1x*dx + v1y*dy;
	    float dotProduct2 = v2x*dx + v2y*dy;

	    // Обмен компонентами скорости вдоль нормали
	    float newV1x = v1x - dotProduct1 * dx + dotProduct2 * dx;
	    float newV1y = v1y - dotProduct1 * dy + dotProduct2 * dy;
	    float newV2x = v2x - dotProduct2 * dx + dotProduct1 * dx;
	    float newV2y = v2y - dotProduct2 * dy + dotProduct1 * dy;

	    // Обновляем углы движения
	    obj1.angle = (int) Math.toDegrees(Math.atan2(newV1x, newV1y));
	    obj2.angle = (int) Math.toDegrees(Math.atan2(newV2x, newV2y));
	    
	    // Разделяем объекты, чтобы избежать залипания
	    float overlap = (obj1.solidArea.width/2f + obj2.solidArea.width/2f) - distance;
	    if (overlap > 0) {
	        float separateX = dx * overlap * 0.5f;
	        float separateY = dy * overlap * 0.5f;
	        
	        obj1.x -= separateX;
	        obj1.y -= separateY;
	        obj2.x += separateX;
	        obj2.y += separateY;
	    }
	}
}